package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.DeniedPermissionException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.model.dto.authorDTO.AuthorWithNameDTO;
import com.example.goodreads.model.dto.bookDTO.*;
import com.example.goodreads.model.entities.*;
import com.example.goodreads.model.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

@Service
public class BookService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookshelfRepository bookshelfRepository;
    @Autowired
    private UsersBooksRepository usersBooksRepository;
    @Autowired
    private ObjectMapper objMapper;
    @Autowired
    private ModelMapper mapper;

    private static final String COVER_PHOTOS = "cover_photos";

    @SneakyThrows
    @Transactional
    public BookResponseDTO addBook(String bookInfo, MultipartFile cover, long loggedUserId) {
        Book b = addNewBook(bookInfo, cover, loggedUserId);
        return mapper.map(b, BookResponseDTO.class);
    }

    @Transactional
    public BookResponseDTO addEdition(long bookId, String bookInfo, MultipartFile cover, long loggedUserId) {
        Book originalBook = bookRepository
                .findById(bookId)
                .orElseThrow(() -> (new NotFoundException("Book not found!")));
        Book newEdition = addNewBook(bookInfo, cover, loggedUserId);

        // Create book-edition record in DB
        Set<Book> originalBookEditions = originalBook.getEditions();
        if (originalBookEditions == null) {
            originalBookEditions = new HashSet<>();
        }
        originalBookEditions.add(newEdition);

        // Create edition-book record in DB
        Set<Book> editions = new HashSet<>();
        editions.add(originalBook);
        newEdition.setEditions(editions);

        return mapper.map(newEdition, BookResponseDTO.class);
    }

    @Transactional
    public BookResponseDTO addToShelf(AddBookToShelfDTO bookDTO, long userId) {
        if (bookDTO == null) {
            throw new BadRequestException("Parameters cannot be null!");
        }
        Book book = bookRepository.findById(bookDTO.getBookId())
                .orElseThrow(() -> (new NotFoundException("Book not found!")));
        Bookshelf bookshelf = bookshelfRepository.findById(bookDTO.getBookshelfId())
                .orElseThrow(() -> (new NotFoundException("Bookshelf not found!")));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> (new NotFoundException("User not found!")));
        Optional<UsersBooks> opt = usersBooksRepository.findByBookAndUser(book, user);
        if (opt.isPresent()) {
            if (opt.get().getBookshelf() == bookshelf) {
                return mapper.map(book, BookResponseDTO.class);
            }
            usersBooksRepository.deleteByBookAndUser(book, user);
        }
        UsersBooks record = new UsersBooks();
        record.setBook(book);
        record.setUser(user);
        record.setBookshelf(bookshelf);
        UsersBooksKey key = new UsersBooksKey();
        key.setBookId(book.getBookId());
        key.setUserId(userId);
        record.setId(key);
        BookResponseDTO booksDTO = mapper.map(book, BookResponseDTO.class);
        usersBooksRepository.save(record);
        return booksDTO;
    }

    public List<SearchBookDTO> searchBooksByTitle(String searchWord) {
        validateSearchWord(searchWord);
        List<Book> books = bookRepository.findBooksByTitleLike("%" + searchWord + "%");
        return extractDTOList(books);
    }

    public List<SearchBookDTO> searchBooksByAuthor(String searchWord) {
        validateSearchWord(searchWord);
        List<Book> books = bookRepository.findBooksByAuthorNameLike("%" + searchWord + "%");
        return extractDTOList(books);
    }

    public List<SearchBookDTO> searchBooksByGenre(long genreId) {
        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> (new NotFoundException("Genre not found!")));
        List<Book> books = bookRepository.findBooksByGenre(genre);
        return extractDTOList(books);
    }

    private List<SearchBookDTO> extractDTOList(List<Book> books) {
        if (books == null) {
            throw new NotFoundException("Books not found!");
        }
        List<SearchBookDTO> dtoList = new ArrayList<>();
        for (Book book : books) {
            SearchBookDTO dto = SearchBookDTO.builder()
                    .bookId(book.getBookId())
                    .title(book.getTitle())
                    .ratings(book.getRatings().size())
                    .published(book.getPublishDate().getYear())
                    .editionsNumber(book.getEditions().size())
                    .build();
            List<String> authors = new ArrayList<>();
            book.getAuthors().forEach(author -> authors.add(author.getAuthorName()));
            dto.setAuthors(authors);
            if (dto.getRatings() == 0) {
                dto.setAvgRating(0.0);
            } else {
                int sumRatings = book.getRatings().stream().mapToInt(Rating::getRating).sum();
                dto.setAvgRating(sumRatings * 1.0 / dto.getRatings());
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    public GetBookDTO getBook(long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> (new NotFoundException("Book not found!")));
        GetBookDTO bookDTO = mapper.map(book, GetBookDTO.class);
        bookDTO.setRatingsNumber(book.getRatings().size());
        bookDTO.setReviewsNumber(book.getReviews().size());
        bookDTO.setEditionsNumber(book.getEditions().size());
        if (bookDTO.getRatingsNumber() == 0) {
            bookDTO.setAvgRating(0.0);
        } else {
            int sumRatings = book.getRatings().stream().mapToInt(Rating::getRating).sum();
            bookDTO.setAvgRating(sumRatings * 1.0 / bookDTO.getRatingsNumber());
        }
        return bookDTO;
    }

    public File getCover(long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> (new NotFoundException("Book not found!")));
        File f = new File(COVER_PHOTOS + File.separator + book.getCoverUrl());
        if(!f.exists()){
            throw new NotFoundException("Cover file does not exist");
        }
        return f;
    }

    @Transactional
    public BookResponseDTO deleteBook(long bookId, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if (!user.getIsAdmin()) {
            throw new DeniedPermissionException("Operation is not allowed!");
        }
        Book book = bookRepository.findById(bookId).orElseThrow(() -> (new NotFoundException("Book not found!")));
        bookRepository.delete(book);
        BookResponseDTO dto = mapper.map(book, BookResponseDTO.class);
        return dto;
    }

    @SneakyThrows
    private Book addNewBook(String bookInfo, MultipartFile cover, long loggedUserId) {
        if (cover == null) {
            throw new BadRequestException("There is no book cover provided!");
        }
        if (bookInfo == null) {
            throw new BadRequestException("There are no book properties provided!");
        }
        User user = userRepository.findById(loggedUserId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if (!user.getIsAdmin()) {
            throw new DeniedPermissionException("Operation is not allowed!");
        }
        AddBookDTO dto = objMapper.readValue(bookInfo, AddBookDTO.class);
        if (!dto.isValid()) {
            throw new BadRequestException("Invalid book properties provided!");
        }
        if (bookRepository.findBookByISBN(dto.getIsbn()).isPresent()) {
            throw new BadRequestException("A book with such ISBN already exists!");
        }
        Genre genre = genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> (new NotFoundException("A genre with such ID does not exist!")));
        Language language = languageRepository.findById(dto.getLanguageId())
                .orElseThrow(() -> (new NotFoundException("A language with such ID does not exist!")));
        Book b = Book.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .pages(dto.getPages())
                .ISBN(dto.getIsbn())
                .originalTitle(dto.getOriginalTitle())
                .publishDate(dto.getPublishDate())
                .publisher(dto.getPublisher())
                .genre(genre)
                .language(language)
                .build();
        AuthorWithNameDTO[] authors = dto.getAuthorsWithName();
        Set<Author> bookAuthors = new HashSet<>();
        for (AuthorWithNameDTO author : authors) {
            if (author != null) {
                Optional<Author> opt = authorRepository.findById(author.getAuthorId());
                if (opt.isPresent() && opt.get().getAuthorName().equals(author.getAuthorName().trim())) {
                    Author currentAuthor = opt.get();
                    bookAuthors.add(currentAuthor);
                } else {
                    if (!author.getAuthorName().isBlank()) {
                        Author newAuthor = new Author();
                        newAuthor.setAuthorName(author.getAuthorName().trim());
                        authorRepository.save(newAuthor);
                        bookAuthors.add(newAuthor);
                    }
                }
            }
        }
        b.setAuthors(bookAuthors);
        String extension = FilenameUtils.getExtension(cover.getOriginalFilename());
        String coverName = System.nanoTime() + "." + extension;
        Files.copy(cover.getInputStream(), new File(COVER_PHOTOS + File.separator + coverName).toPath());
        b.setCoverUrl(coverName);
        return bookRepository.save(b);
    }

    private void validateSearchWord(String searchWord) {
        if (searchWord == null || searchWord.isBlank()) {
            throw new BadRequestException("Invalid search parameters provided!");
        }
    }

}

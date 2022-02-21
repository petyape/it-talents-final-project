package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.DeniedPermissionException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.model.authorDTO.AuthorWithNameDTO;
import com.example.goodreads.model.bookDTO.AddBookDTO;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    private ObjectMapper objMapper;

    @SneakyThrows
    @Transactional
    public Book addBook(String json, MultipartFile cover, long loggedUserId) {
        if (cover == null) {
            throw new BadRequestException("There is no book cover provided!");
        }
        if (json == null) {
            throw new BadRequestException("There are no book properties provided!");
        }
        User user = userRepository.findById(loggedUserId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if (!user.getIsAdmin()) {
            throw new DeniedPermissionException("Operation is not allowed!");
        }
        AddBookDTO dto = objMapper.readValue(json, AddBookDTO.class);
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
                if (opt.isPresent() && opt.get().getAuthorName().equals(author.getAuthorName())) {
                    b.getAuthors().add(opt.get());
                } else {
                    if (!author.getAuthorName().isBlank()) {
                        Author newAuthor = new Author();
                        newAuthor.setAuthorName(author.getAuthorName());
                        authorRepository.save(newAuthor);
                        bookAuthors.add(newAuthor);
                    }
                }
            }
        }
        b.setAuthors(bookAuthors);
        String extension = FilenameUtils.getExtension(cover.getOriginalFilename());
        String coverName = System.nanoTime() + "." + extension;
        Files.copy(cover.getInputStream(), new File("cover_photos" + File.separator + coverName).toPath());
        b.setCoverUrl(coverName);
        return bookRepository.save(b);
    }
}

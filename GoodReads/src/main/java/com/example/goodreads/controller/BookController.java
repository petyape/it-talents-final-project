package com.example.goodreads.controller;

import com.example.goodreads.model.dto.bookDTO.AddBookToShelfDTO;
import com.example.goodreads.model.dto.bookDTO.BookResponseDTO;
import com.example.goodreads.model.dto.bookDTO.SearchBookDTO;
import com.example.goodreads.model.entities.Book;
import com.example.goodreads.services.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BookController extends BaseController {
    @Autowired
    private BookService bookService;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/books/add")
    public ResponseEntity<BookResponseDTO> addBook(@RequestParam(name = "user_book") String json,
                                                   @RequestParam(name = "cover") MultipartFile cover,
                                                   HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        Book b = bookService.addBook(json, cover, (long)session.getAttribute(USER_ID));
        BookResponseDTO dto = mapper.map(b, BookResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/book/add_edition/{book_id}")
    public ResponseEntity<BookResponseDTO> addEdition(@PathVariable long book_id,
                                                      @RequestParam(name = "user_book") String json,
                                                      @RequestParam(name = "cover") MultipartFile cover,
                                                      HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        Book b = bookService.addEdition(book_id, json, cover, (long)session.getAttribute(USER_ID));
        BookResponseDTO dto = mapper.map(b, BookResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/book/add_to_shelf")
    public ResponseEntity<BookResponseDTO> addToShelf(@RequestBody AddBookToShelfDTO bookDTO,
                                                      HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        Book b = bookService.addToShelf(bookDTO, (long)session.getAttribute(USER_ID));
        BookResponseDTO dto = mapper.map(b, BookResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/search/by_title/{title}")
    public ResponseEntity<List<SearchBookDTO>> searchBooksByTitle(@PathVariable String title, HttpSession session) {
        validateSession(session);
        List<SearchBookDTO> responseList = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/search/by_author/{author}")
    public ResponseEntity<List<SearchBookDTO>> searchBooksByAuthor(@PathVariable String author, HttpSession session) {
        validateSession(session);
        List<SearchBookDTO> responseList = bookService.searchBooksByAuthor(author);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/search/by_genre/{id}")
    public ResponseEntity<List<SearchBookDTO>> searchBooksByGenre(@PathVariable long id, HttpSession session) {
        validateSession(session);
        List<SearchBookDTO> responseList = bookService.searchBooksByGenre(id);
        return ResponseEntity.ok(responseList);
    }

//    @GetMapping("/book/show/{id}")

}

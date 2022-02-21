package com.example.goodreads.controller;

import com.example.goodreads.model.dto.bookDTO.AddBookToShelfDTO;
import com.example.goodreads.model.dto.bookDTO.BookResponseDTO;
import com.example.goodreads.model.entities.Book;
import com.example.goodreads.services.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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


//    @PostMapping("/book/rate")
//    @PostMapping("/book/add_review")

//    @GetMapping("/book/show/{id}")
//    @GetMapping("/book/ratings")
//    @GetMapping("/book/reviews")
//    @GetMapping("/search/by_title")
//    @GetMapping("/search/by_genre")
//    @GetMapping("/search/by_author")

}

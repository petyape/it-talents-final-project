package com.example.goodreads.controller;

import com.example.goodreads.model.bookDTO.AddBookDTO;
import com.example.goodreads.model.bookDTO.BookResponseDTO;
import com.example.goodreads.model.entities.Book;
import com.example.goodreads.services.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        Book b = bookService.addBook(json, cover, session);
        BookResponseDTO dto = mapper.map(b, BookResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

}

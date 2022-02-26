package com.example.goodreads.controller;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.model.dto.PageDTO;
import com.example.goodreads.model.dto.bookDTO.AddBookToShelfDTO;
import com.example.goodreads.model.dto.bookDTO.BookResponseDTO;
import com.example.goodreads.model.dto.bookDTO.GetBookDTO;
import com.example.goodreads.model.dto.bookDTO.SearchBookDTO;
import com.example.goodreads.services.BookService;
import com.example.goodreads.services.util.Helper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

@RestController
public class BookController extends BaseController {

    @Autowired
    private BookService bookService;

    @PostMapping("/books/add")
    public ResponseEntity<BookResponseDTO> addBook(@RequestParam(name = "book_info") String bookInfo,
                                                   @RequestParam(name = "cover") MultipartFile cover,
                                                   HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        Helper.validateFile(cover);
        BookResponseDTO dto = bookService.addBook(bookInfo, cover, (long)session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/books/add_edition/{book_id}")
    public ResponseEntity<BookResponseDTO> addEdition(@PathVariable long book_id,
                                                      @RequestParam(name = "book_info") String bookInfo,
                                                      @RequestParam(name = "cover") MultipartFile cover,
                                                      HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        Helper.validateFile(cover);
        BookResponseDTO dto = bookService.addEdition(book_id, bookInfo, cover, (long)session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/books/add_to_shelf")
    public ResponseEntity<BookResponseDTO> addToShelf(@RequestBody AddBookToShelfDTO bookDTO,
                                                      HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        BookResponseDTO dto = bookService.addToShelf(bookDTO, (long)session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/search/by_title/{title}")
    public ResponseEntity<PageDTO> searchBooksByTitle(@RequestParam int page,
                                                                  @PathVariable String title, HttpSession session) {
        if (title == null || title.isBlank()) {
            throw new BadRequestException("Missing keyword!");
        }
        Helper.validatePage(page);
        validateSession(session);
        List<SearchBookDTO> responseList = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(Helper.createPage(responseList, page));
    }

    @GetMapping("/search/by_author/{author}")
    public ResponseEntity<PageDTO> searchBooksByAuthor(@RequestParam int page,
                                                       @PathVariable String author, HttpSession session) {
        if (author == null || author.isBlank()) {
            throw new BadRequestException("Missing keyword!");
        }
        Helper.validatePage(page);
        validateSession(session);
        List<SearchBookDTO> responseList = bookService.searchBooksByAuthor(author);
        return ResponseEntity.ok(Helper.createPage(responseList, page));
    }

    @GetMapping("/search/by_genre/{id}")
    public ResponseEntity<PageDTO> searchBooksByGenre(@RequestParam int page,
                                                                  @PathVariable long id, HttpSession session) {
        Helper.validatePage(page);
        validateSession(session);
        List<SearchBookDTO> responseList = bookService.searchBooksByGenre(id);
        return ResponseEntity.ok(Helper.createPage(responseList, page));
    }

    @SneakyThrows
    @GetMapping("/books/show/{id}")
    public ResponseEntity<GetBookDTO> getBook(@PathVariable long id,HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        GetBookDTO bookDTO = bookService.getBook(id);
        return ResponseEntity.ok(bookDTO);
    }

    @SneakyThrows
    @GetMapping("/books/cover/{id}")
    public void getCover(@PathVariable long id, HttpSession session, HttpServletResponse response) {
        validateSession(session);
        File cover = bookService.getCover(id);
        Files.copy(cover.toPath(), response.getOutputStream());
    }

    @DeleteMapping("/books/delete/{id}")
    public ResponseEntity<BookResponseDTO> deleteBook(@PathVariable long id, HttpSession session, HttpServletRequest request){
        validateSession(session, request);
        BookResponseDTO dto = bookService.deleteBook(id, (long) session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

}

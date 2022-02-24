package com.example.goodreads.controller;

import com.example.goodreads.model.dto.quoteDTO.AddQuoteDTO;
import com.example.goodreads.model.dto.quoteDTO.QuoteResponseDTO;
import com.example.goodreads.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class QuoteController extends BaseController {

    @Autowired
    private QuoteService quoteService;

    @PostMapping("/quotes/new")
    public ResponseEntity<QuoteResponseDTO> addQuote(@RequestBody AddQuoteDTO quoteDTO,
                                                     HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        QuoteResponseDTO dto = quoteService.addQuote(quoteDTO, (long)session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);

    }

    @PutMapping("/quotes/react/{quote_id}")
    public ResponseEntity<QuoteResponseDTO> reactOnQuote(@PathVariable long quote_id,
                                                         HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        QuoteResponseDTO dto = quoteService.reactOnQuote(quote_id, (long)session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/quotes")
    public ResponseEntity<List<QuoteResponseDTO>> getAllQuotes(HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        List<QuoteResponseDTO> dtoList = quoteService.getAllQuotes();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/quotes/fav")
    public ResponseEntity<List<QuoteResponseDTO>> getFavQuotes(HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        List<QuoteResponseDTO> dtoList = quoteService.getFavQuotes((long) session.getAttribute(USER_ID));
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/quotes/my_quotes")
    public ResponseEntity<List<QuoteResponseDTO>> getMyQuotes(HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        List<QuoteResponseDTO> dtoList = quoteService.getMyQuotes((long) session.getAttribute(USER_ID));
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("quotes/delete/{id}")
    public ResponseEntity<String> deleteQuote(@PathVariable long id, HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        String message = quoteService.deleteQuote(id, (long) session.getAttribute(USER_ID));
        return ResponseEntity.ok(message);
    }

}

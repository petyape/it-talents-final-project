package com.example.goodreads.controller;

import com.example.goodreads.model.dto.PageDTO;
import com.example.goodreads.model.dto.ratingDTO.RateBookDTO;
import com.example.goodreads.model.dto.ratingDTO.RatingResponseDTO;
import com.example.goodreads.model.dto.ratingDTO.RatingWithUserDTO;
import com.example.goodreads.model.dto.ratingDTO.UserRatingsResponseDTO;
import com.example.goodreads.services.RatingService;
import com.example.goodreads.services.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class RatingController extends BaseController {

    @Autowired
    private RatingService ratingService;

    @PostMapping("/books/rate")
    public ResponseEntity<RatingResponseDTO> rateBook(@RequestBody RateBookDTO ratingDTO,
                                                      HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        RatingResponseDTO dto = ratingService.rateBook(ratingDTO, (long)session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/books/ratings/{id}")
    public ResponseEntity<PageDTO> getBookRatings(@RequestParam int page, @PathVariable long id, HttpSession session) {
        validateSession(session);
        Helper.validatePage(page);
        List<RatingWithUserDTO> responseList = ratingService.getBookRatings(id);
        return ResponseEntity.ok(Helper.createPage(responseList, page));
    }

    @GetMapping("/users/ratings/{id}")
    public ResponseEntity<PageDTO> getUserRatings(@RequestParam int page, @PathVariable long id, HttpSession session) {
        validateSession(session);
        Helper.validatePage(page);
        List<UserRatingsResponseDTO> responseList = ratingService.getUserRatings(id);
        return ResponseEntity.ok(Helper.createPage(responseList, page));
    }

}

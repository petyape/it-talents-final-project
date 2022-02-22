package com.example.goodreads.controller;

import com.example.goodreads.model.dto.ratingDTO.RatingResponseDTO;
import com.example.goodreads.model.entities.Rating;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class ReviewController extends BaseController {

//    @PostMapping("/book/add_review")
//    public ResponseEntity<ReviewResponseDTO> addReview(@RequestBody AddReviewDTO bookDTO,
//                                                       HttpSession session, HttpServletRequest request) {
//        validateSession(session, request);
//        Rating r = bookService.addReview(bookDTO, (long)session.getAttribute(USER_ID));
//        RatingResponseDTO dto = mapper.map(r, RatingResponseDTO.class);
//        return ResponseEntity.ok(dto);
//    }

//    @GetMapping("/book/reviews")
}

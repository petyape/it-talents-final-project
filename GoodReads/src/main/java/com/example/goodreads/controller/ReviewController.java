package com.example.goodreads.controller;

import com.example.goodreads.model.dto.PageDTO;
import com.example.goodreads.model.dto.reviewDTO.*;
import com.example.goodreads.services.ReviewService;
import com.example.goodreads.services.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ReviewController extends BaseController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/books/add_review")
    public ResponseEntity<ReviewResponseDTO> addReview(@RequestBody AddReviewDTO reviewDTO,
                                                       HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        ReviewResponseDTO dto = reviewService.addReview(reviewDTO, (long) session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/books/reviews/{id}")
    public ResponseEntity<PageDTO> getBookReviews(@RequestParam int page, @PathVariable long id, HttpSession session) {
        validateSession(session);
        Helper.validatePage(page);
        List<ReviewWithUserDTO> responseList = reviewService.getBookReviews(id);
        return ResponseEntity.ok(Helper.createPage(responseList, page));
    }

    @GetMapping("/users/reviews/{id}")
    public ResponseEntity<PageDTO> getUserReviews(@RequestParam int page, @PathVariable long id, HttpSession session, HttpServletRequest request) {
        validateSession(session);
        Helper.validatePage(page);
        List<UserReviewsResponseDTO> responseList = reviewService.getUserReviews(id);
        return ResponseEntity.ok(Helper.createPage(responseList, page));
    }

}

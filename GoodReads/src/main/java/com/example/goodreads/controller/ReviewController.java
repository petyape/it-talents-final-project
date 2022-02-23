package com.example.goodreads.controller;

import com.example.goodreads.model.dto.ratingDTO.UserRatingsResponseDTO;
import com.example.goodreads.model.dto.reviewDTO.*;
import com.example.goodreads.model.entities.Review;
import com.example.goodreads.services.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReviewController extends BaseController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/book/add_review")
    public ResponseEntity<ReviewResponseDTO> addReview(@RequestBody AddReviewDTO reviewDTO,
                                                       HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        Review r = reviewService.addReview(reviewDTO, (long) session.getAttribute(USER_ID));
        ReviewResponseDTO dto = mapper.map(r, ReviewResponseDTO.class);
        dto.setTitle(r.getBook().getTitle());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/book/reviews/{id}")
    public ResponseEntity<List<ReviewWithUserDTO>> getBookReviews(@PathVariable long id, HttpSession session) {
        validateSession(session);
        List<Review> reviews = reviewService.getBookReviews(id);
        List<ReviewWithUserDTO> responseList = new ArrayList<>();
        reviews.forEach(r -> {
            ReviewWithUserDTO dto = mapper.map(r, ReviewWithUserDTO.class);
            dto.setName(r.getUser().getFirstName());
            responseList.add(dto);
        });
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/user/reviews/{id}")
    public ResponseEntity<List<UserReviewsResponseDTO>> getUserReviews(@PathVariable long id, HttpSession session, HttpServletRequest request) {
        validateSession(session);
        List<UserReviewsResponseDTO> responseList = reviewService.getUserReviews(id);
        return ResponseEntity.ok(responseList);
    }
}

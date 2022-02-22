package com.example.goodreads.controller;

import com.example.goodreads.model.dto.ratingDTO.RatingResponseDTO;
import com.example.goodreads.model.dto.reviewDTO.AddReviewDTO;
import com.example.goodreads.model.dto.reviewDTO.ReviewResponseDTO;
import com.example.goodreads.model.entities.Rating;
import com.example.goodreads.model.entities.Review;
import com.example.goodreads.services.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        Review r = reviewService.addReview(reviewDTO, (long)session.getAttribute(USER_ID));
        ReviewResponseDTO dto = mapper.map(r, ReviewResponseDTO.class);
        dto.setTitle(r.getBook().getTitle());
        return ResponseEntity.ok(dto);
    }

//    @GetMapping("/book/reviews")
}

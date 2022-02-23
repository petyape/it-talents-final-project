package com.example.goodreads.controller;

import com.example.goodreads.model.dto.ratingDTO.RateBookDTO;
import com.example.goodreads.model.dto.ratingDTO.RatingResponseDTO;
import com.example.goodreads.model.dto.ratingDTO.RatingWithUserDTO;
import com.example.goodreads.model.dto.ratingDTO.UserRatingsResponseDTO;
import com.example.goodreads.model.entities.Rating;
import com.example.goodreads.services.RatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RatingController extends BaseController {
    @Autowired
    private RatingService ratingService;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/book/rate")
    public ResponseEntity<RatingResponseDTO> rateBook(@RequestBody RateBookDTO ratingDTO,
                                                      HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        Rating r = ratingService.rateBook(ratingDTO, (long)session.getAttribute(USER_ID));
        RatingResponseDTO dto = mapper.map(r, RatingResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/book/ratings/{id}")
    public ResponseEntity<List<RatingWithUserDTO>> getBookRatings(@PathVariable long id, HttpSession session) {
        validateSession(session);
        List<RatingWithUserDTO> responseList = ratingService.getBookRatings(id);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/user/ratings/{id}")
    public ResponseEntity<List<UserRatingsResponseDTO>> getUserRatings(@PathVariable long id, HttpSession session) {
        validateSession(session);
        List<UserRatingsResponseDTO> responseList = ratingService.getUserRatings(id);
        return ResponseEntity.ok(responseList);
    }

}

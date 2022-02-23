package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.model.dto.ratingDTO.UserRatingsResponseDTO;
import com.example.goodreads.model.dto.reviewDTO.AddReviewDTO;
import com.example.goodreads.model.dto.reviewDTO.UserReviewsResponseDTO;
import com.example.goodreads.model.entities.Book;
import com.example.goodreads.model.entities.Rating;
import com.example.goodreads.model.entities.Review;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.repository.BookRepository;
import com.example.goodreads.model.repository.ReviewRepository;
import com.example.goodreads.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;

    public Review addReview(AddReviewDTO reviewDTO, long userId) {
        if (reviewDTO == null) {
            throw new BadRequestException("Invalid parameters!");
        }
        Book book = bookRepository.findById(reviewDTO.getBookId())
                .orElseThrow(() -> (new NotFoundException("Book not found!")));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> (new NotFoundException("User not found!")));
        if (reviewDTO.getReview() == null || reviewDTO.getReview().isBlank()) {
            throw new BadRequestException("Invalid review parameters!");
        }
        Optional<Review> opt = reviewRepository.findByBookAndUser(book, user);
        Review review;
        if (opt.isPresent()) {
            review = opt.get();
        }
        else {
            review = new Review();
            review.setBook(book);
            review.setUser(user);
            review.setReviewDate(LocalDate.now());
        }
        review.setReview(reviewDTO.getReview());
        return reviewRepository.save(review);
    }

    public List<Review> getBookReviews(long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> (new NotFoundException("Book not found!")));
        return reviewRepository.findAllByBook(book);
    }

    public List<UserReviewsResponseDTO> getUserReviews(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        List<Review> reviews = reviewRepository.findAllByUser(user);
        List<UserReviewsResponseDTO> responseList = new ArrayList<>();
        reviews.forEach(r -> {
            UserReviewsResponseDTO dto = mapper.map(r, UserReviewsResponseDTO.class);
            dto.setTitle(r.getBook().getTitle());
            dto.setFirstName(r.getUser().getFirstName());
            responseList.add(dto);
        });
        return responseList;
    }

}

package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.model.dto.ratingDTO.RateBookDTO;
import com.example.goodreads.model.entities.Book;
import com.example.goodreads.model.entities.Rating;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.repository.BookRepository;
import com.example.goodreads.model.repository.RatingRepository;
import com.example.goodreads.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    public Rating rateBook(RateBookDTO ratingDTO, long userId) {
        if (ratingDTO == null) {
            throw new BadRequestException("Invalid parameters!");
        }
        Book book = bookRepository.findById(ratingDTO.getBookId())
                .orElseThrow(() -> (new NotFoundException("Book not found!")));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> (new NotFoundException("User not found!")));
        if (ratingDTO.getRating() < 1 || ratingDTO.getRating() > 5) {
            throw new BadRequestException("Invalid rating parameters!");
        }
        Optional<Rating> opt = ratingRepository.findByBookAndUser(book, user);
        Rating rating;
        if (opt.isPresent()) {
            rating = opt.get();
            if (rating.getRating() == ratingDTO.getRating()) {
                return rating;
            }
        }
        else {
            rating = new Rating();
            rating.setBook(book);
            rating.setUser(user);
        }
        rating.setRating(ratingDTO.getRating());
        return ratingRepository.save(rating);
    }
}

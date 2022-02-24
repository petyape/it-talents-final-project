package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.model.dto.ratingDTO.RateBookDTO;
import com.example.goodreads.model.dto.ratingDTO.RatingResponseDTO;
import com.example.goodreads.model.dto.ratingDTO.RatingWithUserDTO;
import com.example.goodreads.model.dto.ratingDTO.UserRatingsResponseDTO;
import com.example.goodreads.model.entities.Book;
import com.example.goodreads.model.entities.Rating;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.repository.BookRepository;
import com.example.goodreads.model.repository.RatingRepository;
import com.example.goodreads.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ModelMapper mapper;

    public RatingResponseDTO rateBook(RateBookDTO ratingDTO, long userId) {
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
                return mapper.map(rating, RatingResponseDTO.class);
            }
        }
        else {
            rating = new Rating();
            rating.setBook(book);
            rating.setUser(user);
        }
        rating.setRating(ratingDTO.getRating());
        return mapper.map(ratingRepository.save(rating), RatingResponseDTO.class);
    }

    public List<RatingWithUserDTO> getBookRatings(long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> (new NotFoundException("Book not found!")));
        List<Rating> ratings = ratingRepository.findAllByBook(book);
        List<RatingWithUserDTO> responseList = new ArrayList<>();
        ratings.forEach(r -> {
            RatingWithUserDTO dto = mapper.map(r, RatingWithUserDTO.class);
            dto.setName(r.getUser().getFirstName());
            responseList.add(dto);
        });
        return responseList;
    }

    public List<UserRatingsResponseDTO> getUserRatings(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        List<Rating> ratings = ratingRepository.findAllByUser(user);
        List<UserRatingsResponseDTO> responseList = new ArrayList<>();
        ratings.forEach(r -> {
            UserRatingsResponseDTO dto = mapper.map(r, UserRatingsResponseDTO.class);
            dto.setTitle(r.getBook().getTitle());
            dto.setFirstName(r.getUser().getFirstName());
            responseList.add(dto);
        });
        return responseList;
    }

}

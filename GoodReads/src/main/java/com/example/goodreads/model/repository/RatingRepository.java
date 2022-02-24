package com.example.goodreads.model.repository;

import com.example.goodreads.model.entities.Book;
import com.example.goodreads.model.entities.Rating;
import com.example.goodreads.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByBookAndUser(Book book, User user);

    List<Rating> findAllByBook(Book book);

    List<Rating> findAllByUser(User user);

}

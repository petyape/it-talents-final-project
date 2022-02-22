package com.example.goodreads.model.repository;
import com.example.goodreads.model.entities.Book;
import com.example.goodreads.model.entities.Review;
import com.example.goodreads.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByBookAndUser(Book book, User user);

}

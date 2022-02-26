package com.example.goodreads.model.repository;

import com.example.goodreads.model.entities.Book;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.entities.UsersBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsersBooksRepository extends JpaRepository<UsersBooks, Long> {

    Optional<UsersBooks> findByBookAndUser(Book book, User user);

    void deleteByBookAndUser(Book book, User user);

    List<UsersBooks> findByUser(User user);

}

package com.example.goodreads.model.repository;

import com.example.goodreads.model.entities.Book;
import com.example.goodreads.model.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookByISBN(String ISBN);
    List<Book> findBooksByGenre(Genre genre);
    List<Book> findBooksByTitleLike(String searchWord);

    @Query(value = "SELECT * FROM books AS b " +
                   "JOIN books_have_authors AS bha " +
                   "ON (bha.book_id = b.book_id) " +
                   "JOIN authors AS a " +
                   "ON (a.author_id = bha.author_id) " +
                   "WHERE a.author_name LIKE ?1",
                    nativeQuery = true)
    List<Book> findBooksByAuthorNameLike(String searchWord);
}

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

    @Query(value = "SELECT * FROM books AS b " +
            "JOIN users_have_books AS uhb " +
            "ON (uhb.book_id = b.book_id) " +
            "JOIN bookshelves AS sh " +
            "ON (sh.bookshelf_id = uhb.bookshelf_id) " +
            "JOIN users AS u " +
            "ON (u.user_id = uhb.user_id) " +
            "WHERE (uhb.user_id = ?1 " +
            "AND uhb.bookshelf_id = ?2)",
            nativeQuery = true)
    List<Book> findBooksByUserIdAndBookshelfId(long userId, long bookshelfId);

    @Query(value = "SELECT count(book_id) " +
            "FROM users_have_books " +
            "WHERE (bookshelf_id = ?1) " +
            "AND (user_id = ?2)",
            nativeQuery = true)
    long countBookByBookshelfIdAndUserId(long bookshelf_id, long user_id);

}

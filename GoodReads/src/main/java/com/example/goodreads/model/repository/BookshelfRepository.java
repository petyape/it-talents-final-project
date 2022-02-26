package com.example.goodreads.model.repository;

import com.example.goodreads.model.entities.Bookshelf;
import com.example.goodreads.model.entities.UsersBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookshelfRepository extends JpaRepository<Bookshelf, Long> {

    @Query(value = "SELECT uhb.bookshelf_id, uhb.user_id, uhb.book_id " +
            " FROM users_have_books as UHB " +
            " JOIN bookshelves as B " +
            " ON (uhb.bookshelf_id = b.bookshelf_id) " +
            " WHERE (user_id = ?)",
            nativeQuery = true)
    List<UsersBooks> findBookshelfByUserId(long userId);

}

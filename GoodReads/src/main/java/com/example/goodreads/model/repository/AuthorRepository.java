package com.example.goodreads.model.repository;

import com.example.goodreads.model.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author>findByAuthorNameAndAuthorId(String name, long id);

    Optional<Author>findByAuthorName(String name);

}

package com.example.goodreads.model.repository;

import com.example.goodreads.model.entities.ReadingChallenge;
import com.example.goodreads.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReadingChallengeRepository extends JpaRepository<ReadingChallenge, Long> {

    Optional<ReadingChallenge> findReadingChallengeByUser(User user);
    void deleteByUser(User user);

}

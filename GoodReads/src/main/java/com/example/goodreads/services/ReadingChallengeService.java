package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.model.dto.readingChallengeDTO.EnterChallengeDTO;
import com.example.goodreads.model.entities.ReadingChallenge;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.repository.ReadingChallengeRepository;
import com.example.goodreads.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReadingChallengeService {
    @Autowired
    private ReadingChallengeRepository readingChallengeRepository;
    @Autowired
    private UserRepository userRepository;

    public ReadingChallenge enterChallenge(EnterChallengeDTO entryRequest, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if (entryRequest.getBooksGoal() < 1) {
            throw new BadRequestException("Invalid books goal provided!");
        }
        Optional<ReadingChallenge> opt = readingChallengeRepository.findReadingChallengeByUser(user);
        ReadingChallenge entry;
        if (opt.isPresent()) {
            entry = opt.get();
        } else {
            entry = new ReadingChallenge();
            entry.setUser(user);
        }
        entry.setBooksGoal(entryRequest.getBooksGoal());
        return readingChallengeRepository.save(entry);
    }

}

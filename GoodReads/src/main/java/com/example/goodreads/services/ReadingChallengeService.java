package com.example.goodreads.services;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.model.dto.readingChallengeDTO.ChallengeResponseDTO;
import com.example.goodreads.model.dto.readingChallengeDTO.EnterChallengeDTO;
import com.example.goodreads.model.dto.readingChallengeDTO.ParticipantDTO;
import com.example.goodreads.model.entities.ReadingChallenge;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.repository.ReadingChallengeRepository;
import com.example.goodreads.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReadingChallengeService {

    @Autowired
    private ReadingChallengeRepository readingChallengeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;

    public ChallengeResponseDTO enterChallenge(EnterChallengeDTO entryRequest, long userId) {
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
        return mapper.map(readingChallengeRepository.save(entry), ChallengeResponseDTO.class);
    }

    public ParticipantDTO quitChallenge(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> (new NotFoundException("User not found!")));
        Optional<ReadingChallenge> opt = readingChallengeRepository.findReadingChallengeByUser(user);
        if (opt.isEmpty()) {
            throw new BadRequestException("User does not participate in challenge!");
        }
        readingChallengeRepository.delete(opt.get());
        ParticipantDTO dto = mapper.map(opt.get(), ParticipantDTO.class);
        dto.setFirstName(opt.get().getUser().getFirstName());
        return dto;
    }

    public List<ParticipantDTO> getParticipants() {
        List<ReadingChallenge> entries = readingChallengeRepository.findAll();
        List<ParticipantDTO> dtoList = new ArrayList<>();
        for (ReadingChallenge entry : entries) {
            ParticipantDTO dto = mapper.map(entry, ParticipantDTO.class);
            dto.setFirstName(entry.getUser().getFirstName());
            dtoList.add(dto);
        }
        return dtoList;
    }

}

package com.example.goodreads.controller;

import com.example.goodreads.model.dto.readingChallengeDTO.ChallengeResponseDTO;
import com.example.goodreads.model.dto.readingChallengeDTO.EnterChallengeDTO;
import com.example.goodreads.model.entities.ReadingChallenge;
import com.example.goodreads.services.ReadingChallengeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class ReadingChallengeController extends BaseController {

    @Autowired
    private ReadingChallengeService challengeService;
    @Autowired
    private ModelMapper mapper;

    @PutMapping("/challenge/enter")
    public ResponseEntity<ChallengeResponseDTO> enterChallenge(@RequestBody EnterChallengeDTO entry,
                                                               HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        ReadingChallenge ch = challengeService.enterChallenge(entry, (long) session.getAttribute(USER_ID));
        ChallengeResponseDTO dto = mapper.map(ch, ChallengeResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

}

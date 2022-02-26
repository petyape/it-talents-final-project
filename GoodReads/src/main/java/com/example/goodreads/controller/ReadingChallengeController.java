package com.example.goodreads.controller;

import com.example.goodreads.model.dto.PageDTO;
import com.example.goodreads.model.dto.readingChallengeDTO.ChallengeResponseDTO;
import com.example.goodreads.model.dto.readingChallengeDTO.EnterChallengeDTO;
import com.example.goodreads.model.dto.readingChallengeDTO.ParticipantDTO;
import com.example.goodreads.services.ReadingChallengeService;
import com.example.goodreads.services.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ReadingChallengeController extends BaseController {

    @Autowired
    private ReadingChallengeService challengeService;

    @PutMapping("/challenge/enter")
    public ResponseEntity<ChallengeResponseDTO> enterChallenge(@RequestBody EnterChallengeDTO entry,
                                                               HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        ChallengeResponseDTO dto = challengeService.enterChallenge(entry, (long) session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/challenge/quit")
    public ResponseEntity<ParticipantDTO> quitChallenge(HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        ParticipantDTO dto = challengeService.quitChallenge((long) session.getAttribute(USER_ID));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/challenge/participants")
    public ResponseEntity<PageDTO> getParticipants(@RequestParam int page, HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        Helper.validatePage(page);
        List<ParticipantDTO> dtoList = challengeService.getParticipants();
        return ResponseEntity.ok(Helper.createPage(dtoList, page));
    }

}

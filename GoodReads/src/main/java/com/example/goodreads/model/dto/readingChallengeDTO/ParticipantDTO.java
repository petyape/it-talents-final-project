package com.example.goodreads.model.dto.readingChallengeDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDTO {

    private long entryId;
    private long userId;
    private String firstName;
    private int booksGoal;

}

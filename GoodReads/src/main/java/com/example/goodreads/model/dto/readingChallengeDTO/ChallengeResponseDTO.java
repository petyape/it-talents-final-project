package com.example.goodreads.model.dto.readingChallengeDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeResponseDTO {

    private long entryId;
    private long userId;
    private int booksGoal;

}

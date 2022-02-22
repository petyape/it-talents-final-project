package com.example.goodreads.model.dto.messageDTO;

import com.example.goodreads.model.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SentMessageDTO {

    private LocalDate sentAt;
    private User sender;
    private User receiver;
    private String message;

}

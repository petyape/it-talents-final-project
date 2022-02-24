package com.example.goodreads.model.dto.messageDTO;

import com.example.goodreads.model.dto.userDTO.UserResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SentMessageDTO {

    private int messageId;
    private LocalDateTime sentAt;
    private UserResponseDTO sender;
    private UserResponseDTO receiver;
    private String message;

}

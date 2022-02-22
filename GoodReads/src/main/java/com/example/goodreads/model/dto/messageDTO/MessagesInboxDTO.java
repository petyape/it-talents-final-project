package com.example.goodreads.model.dto.messageDTO;

import java.time.LocalDate;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MessagesInboxDTO {

    private long messageId;
    private String receiverName;
    private String senderName;
    private String message;
    private LocalDate sentAt;

}

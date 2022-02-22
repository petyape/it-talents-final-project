package com.example.goodreads.model.dto.messageDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SendingMsgDTO {

    private long receiverId;
    private String msg;
}

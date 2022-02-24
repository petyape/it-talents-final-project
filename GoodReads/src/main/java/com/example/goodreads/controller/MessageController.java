package com.example.goodreads.controller;

import com.example.goodreads.model.dto.messageDTO.SendMessageDTO;
import com.example.goodreads.model.dto.messageDTO.SentMessageDTO;
import com.example.goodreads.model.dto.messageDTO.MessagesInboxDTO;
import com.example.goodreads.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class MessageController extends BaseController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/messages/new")
    public ResponseEntity<SentMessageDTO> sendMessage(@RequestBody SendMessageDTO mail,
                                                      HttpSession session, HttpServletRequest request) {
        long receiverId = mail.getReceiverId();
        long senderId = (long) session.getAttribute(USER_ID);
        validateSession(session, request);
        SentMessageDTO dto = messageService.sendMessage(receiverId, senderId, mail.getMessage());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/messages/inbox")
    public ResponseEntity<List<MessagesInboxDTO>> getReceivedMessages(HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        long user = (long) session.getAttribute(USER_ID);
        List<MessagesInboxDTO> messagesReceived = messageService.getReceivedMessages(user);
        return ResponseEntity.ok(messagesReceived);
    }


    @GetMapping("/messages/sent")
    public ResponseEntity<List<MessagesInboxDTO>> getSentMessages(HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        long user = (long) session.getAttribute(USER_ID);
        List<MessagesInboxDTO> messagesSent = messageService.getSentMessages(user);
        return ResponseEntity.ok(messagesSent);
    }

}

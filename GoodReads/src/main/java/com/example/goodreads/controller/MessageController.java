package com.example.goodreads.controller;

import com.example.goodreads.model.dto.messageDTO.SendingMsgDTO;
import com.example.goodreads.model.dto.messageDTO.SentMessageDTO;
import com.example.goodreads.model.dto.messageDTO.MessagesInboxDTO;
import com.example.goodreads.model.entities.Message;
import com.example.goodreads.services.MessageService;
import org.modelmapper.ModelMapper;
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
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/message/new")
    public ResponseEntity<SentMessageDTO> sendMessage(@RequestBody SendingMsgDTO user, HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        long senderId = (long) session.getAttribute(USER_ID);
        String msg = user.getMsg();
        long receiverId = user.getReceiverId();
        Message message = messageService.sendMassage(receiverId, senderId, msg);
        SentMessageDTO dto = mapper.map(message, SentMessageDTO.class);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/message/inbox")
    public ResponseEntity<List<MessagesInboxDTO>> getReceivedMessages(HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        long userId = (long) session.getAttribute(USER_ID);
        List<MessagesInboxDTO> messagesReceived = messageService.getReceivedMessages(userId);
        return ResponseEntity.ok(messagesReceived);
    }


    @GetMapping("/message/sent")
    public ResponseEntity<List<MessagesInboxDTO>> getSentMessages(HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        long userId = (long) session.getAttribute(USER_ID);
        List<MessagesInboxDTO> messagesSent = messageService.getSentMessages(userId);
        return ResponseEntity.ok(messagesSent);
    }
}

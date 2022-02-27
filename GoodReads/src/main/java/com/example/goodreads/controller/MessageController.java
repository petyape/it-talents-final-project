package com.example.goodreads.controller;

import com.example.goodreads.model.dto.PageDTO;
import com.example.goodreads.model.dto.messageDTO.SendMessageDTO;
import com.example.goodreads.model.dto.messageDTO.SentMessageDTO;
import com.example.goodreads.model.dto.messageDTO.MessagesInboxDTO;
import com.example.goodreads.services.MessageService;
import com.example.goodreads.services.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PageDTO> getReceivedMessages(@RequestParam int page, HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        Helper.validatePage(page);
        long user = (long) session.getAttribute(USER_ID);
        List<MessagesInboxDTO> messagesReceived = messageService.getReceivedMessages(user);
        PageDTO pageDTO = Helper.createPage(messagesReceived, page);
        return ResponseEntity.ok(pageDTO);
    }


    @GetMapping("/messages/sent")
    public ResponseEntity<PageDTO> getSentMessages(@RequestParam int page, HttpSession session, HttpServletRequest request) {
        validateSession(session, request);
        Helper.validatePage(page);
        long user = (long) session.getAttribute(USER_ID);
        List<MessagesInboxDTO> messagesSent = messageService.getSentMessages(user);
        PageDTO pageDTO = Helper.createPage(messagesSent, page);
        return ResponseEntity.ok(pageDTO);
    }

}

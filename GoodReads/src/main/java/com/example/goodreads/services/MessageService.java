package com.example.goodreads.services;

import com.example.goodreads.exceptions.DeniedPermissionException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.model.entities.Message;
import com.example.goodreads.model.entities.User;
import com.example.goodreads.model.repository.MessageRepository;
import com.example.goodreads.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public Message sendMassage(long receiverId, long senderId, String msg) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> (new NotFoundException("User not found!")));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> (new NotFoundException("User not found!")));
        if(senderId == receiverId){
            throw new DeniedPermissionException("Sorry, you can't send messages to yourself.");

        }
        if(!receiver.getPrivacy().getPrivateMessages()) {
            throw new DeniedPermissionException("Sorry, this person isn't accepting messages.");
        }
        Message message = createMessage(msg, sender,receiver,LocalDate.now());
        sender.getMessagesSent().add(message);
        receiver.getMessagesReceived().add(message);
        return message;
    }

    private Message createMessage(String msg, User sender, User receiver, LocalDate sendAt){
        Message message = Message.builder()
                .message(msg)
                .sender(sender)
                .receiver(receiver)
                .sentAt(sendAt).build();
        return messageRepository.save(message);
    }
}

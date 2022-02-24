package com.example.goodreads.model.repository;

import com.example.goodreads.model.entities.Message;
import com.example.goodreads.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findMessagesBySender(User sender);

    List<Message> findMessagesByReceiver(User receiver);

}

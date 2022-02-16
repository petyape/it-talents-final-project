package com.example.goodreads.model.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "messages")
public class Message {
    private enum MessageFolder{INBOX, SAVED, SENT, TRASH}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageId;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;

    @Column
    private LocalDate sentAt;
    @Column
    private Boolean isRead;
    @Column
    private MessageFolder receiverFolder;
}

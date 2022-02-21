package com.example.goodreads.model.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;

    @Column
    private LocalDate sentAt;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;
}

package com.example.goodreads.model.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "messages")
public class Message {
    public enum MessageFolder{
        INBOX('i'), SAVED('s'), SENT('f'), TRASH('t');

        public final char symbol;
        MessageFolder(char symbol){
            this.symbol = symbol;
        }

        public static boolean isValidGender(char symbol) {
            return (symbol == INBOX.symbol ||
                    symbol == SAVED.symbol ||
                    symbol == SENT.symbol ||
                    symbol == TRASH.symbol);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;

    @Column
    private LocalDate sentAt;

    @Column
    private Boolean isRead;

    @Column
    private char receiverFolder;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;
}

package com.example.goodreads.model.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "quotes")
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int quoteId;
    @Column
    private String quote;
    @Column
    private String tags;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}

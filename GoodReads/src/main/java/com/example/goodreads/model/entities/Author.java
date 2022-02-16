package com.example.goodreads.model.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authorId;
    @Column
    private String authorName;

// TODO: Many to many
//    @OneToMany(mappedBy = "authors")
//    private Set<Book> books;

    @OneToMany(mappedBy = "authors")
    private Set<Quote> quotes;
}

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

    @OneToMany(mappedBy = "authors")
    private Set<Quote> quotes;

    @ManyToMany(mappedBy = "books_have_authors")
    private Set<Book> books;
}

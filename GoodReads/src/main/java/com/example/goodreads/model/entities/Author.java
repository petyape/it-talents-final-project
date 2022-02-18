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

    @OneToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private Set<Quote> quotes;

    @ManyToMany(mappedBy = "books_have_authors", fetch = FetchType.LAZY)
    private Set<Book> books;
}

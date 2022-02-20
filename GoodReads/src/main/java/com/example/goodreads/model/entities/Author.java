package com.example.goodreads.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "authors")
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long authorId;

    @Column
    private String authorName;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Quote> quotes;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private Set<Book> books;
}

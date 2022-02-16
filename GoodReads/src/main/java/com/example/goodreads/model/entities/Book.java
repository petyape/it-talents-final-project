package com.example.goodreads.model.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private int pages;
    @Column
    private String ISBN;
    @Column
    private String originalTitle;
    @Column
    private LocalDate publishDate;
    @Column
    private String publisher;

    // TODO: cover url

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;
    @OneToMany(mappedBy = "books")
    private Set<Rating> ratings;
    @OneToMany(mappedBy = "books")
    private Set<Review> reviews;
    @OneToMany(mappedBy = "books")
    private Set<Quote> quotes;

// TODO: Many to many
//    private Set<Author> authors;
//    private Set<Book> editions;

}

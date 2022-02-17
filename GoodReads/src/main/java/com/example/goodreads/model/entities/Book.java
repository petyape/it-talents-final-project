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
    @Column
    private String coverUrl;

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
    @OneToMany(mappedBy = "books")
    private Set<UsersBooks> booksPerUser;

    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="books_have_editions",
            joinColumns={@JoinColumn(name="book_id")},
            inverseJoinColumns={@JoinColumn(name="edition_id")})
    private Set<Book> editions;
    @ManyToMany(mappedBy="editions")
    private Set<Book> bookEditions;

    @ManyToMany
    @JoinTable(
            name = "books_have_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;
}

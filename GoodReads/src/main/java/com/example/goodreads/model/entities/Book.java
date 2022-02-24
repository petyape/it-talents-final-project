package com.example.goodreads.model.entities;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "books")
public class Book {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookId;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private int pages;

    @Column(name = "ISBN")
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

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<Rating> ratings;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<Review> reviews;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<UsersBooks> booksPerUser;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "books_have_editions",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "edition_id"))
    private Set<Book> editions;

    @ManyToMany
    @JoinTable(
            name = "books_have_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;

}

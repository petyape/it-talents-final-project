package com.example.goodreads.model.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;
    @Column
    private String review;
    @Column
    private LocalDate reviewDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="reviews_have_comments",
            joinColumns={@JoinColumn(name="review_id")},
            inverseJoinColumns={@JoinColumn(name="comment_id")})
    private Set<Review> comments;
    @ManyToMany(mappedBy="comments")
    private Set<Review> reviews;
}

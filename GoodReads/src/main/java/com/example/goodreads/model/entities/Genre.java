package com.example.goodreads.model.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int genreId;
    @Column
    private String genreName;

    @OneToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private Set<Book> books;

    @ManyToMany(mappedBy = "users_like_genres", fetch = FetchType.LAZY)
    private Set<User> likes;
}

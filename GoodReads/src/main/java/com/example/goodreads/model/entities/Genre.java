package com.example.goodreads.model.entities;

import lombok.Getter;
import javax.persistence.*;
import java.util.Set;

@Getter
@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long genreId;

    @Column
    private String genreName;

    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY)
    private Set<Book> books;

    @ManyToMany(mappedBy = "favoriteGenres", fetch = FetchType.LAZY)
    private Set<User> likes;

}

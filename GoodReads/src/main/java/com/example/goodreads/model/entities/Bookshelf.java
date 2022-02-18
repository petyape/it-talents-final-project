package com.example.goodreads.model.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "bookshelves")
public class Bookshelf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookshelfId;

    @Column
    private String name;

    @OneToMany(mappedBy = "bookshelf", fetch = FetchType.LAZY)
    private Set<UsersBooks> booksPerUser;
}

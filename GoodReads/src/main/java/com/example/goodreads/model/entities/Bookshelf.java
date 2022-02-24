package com.example.goodreads.model.entities;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "bookshelves")
public class Bookshelf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookshelfId;

    @Column
    private String name;

    @OneToMany(mappedBy = "bookshelf", fetch = FetchType.LAZY)
    private Set<UsersBooks> booksPerUser;

}

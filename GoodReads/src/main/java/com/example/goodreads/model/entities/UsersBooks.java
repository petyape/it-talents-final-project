package com.example.goodreads.model.entities;

import javax.persistence.*;

@Entity
public class UsersBooks {

    @EmbeddedId
    UsersBooksKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "bookshelf_id")
    private Bookshelf bookshelf;
}

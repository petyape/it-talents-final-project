package com.example.goodreads.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "bookshelves")
public class Bookshelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookshelfId;
    @Column
    private String name;
}

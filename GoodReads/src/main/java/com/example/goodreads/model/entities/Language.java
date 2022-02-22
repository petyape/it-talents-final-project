package com.example.goodreads.model.entities;
import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Table(name = "languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long languageId;

    @Column
    private String language;

    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
    private Set<Book> books;
}

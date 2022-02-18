package com.example.goodreads.model.entities;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "languages")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int languageId;
    @Column
    private String language;

    @OneToMany(mappedBy = "languages", fetch = FetchType.LAZY)
    private Set<Book> books;
}

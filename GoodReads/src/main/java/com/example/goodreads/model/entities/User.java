package com.example.goodreads.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    private enum Gender{MALE, FEMALE, CUSTOM}
    enum Visibility{EVERYONE, FRIENDS, NONE}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column
    private String email;
    @Column
    private String firstName;
    @Column
    private String middleName;
    @Column
    private String lastName;
    @Column
    @JsonIgnore
    private String password;

    // TODO: photo url

    @Column
    private Gender gender;
    @Column
    private String username;
    @Column
    private Boolean showLastName;
    @Column
    private Boolean isReverseNameOrder;
    @Column
    private Visibility genderViewableBy;
    @Column
    private Visibility locationViewableBy;
    @Column
    private LocalDate dateOfBirth;
    @Column
    private String webSite;
    @Column
    private String interests;
    @Column
    private String booksPreferences;
    @Column
    private String aboutMe;

    @ManyToOne
    @JoinColumn(name = "town_id")
    private Town town;
    @OneToMany(mappedBy = "users")
    private Set<Message> messagesSent;
    @OneToMany(mappedBy = "users")
    private Set<Message> messagesReceived;
    @OneToMany(mappedBy = "users")
    private Set<Review> comments;
    @OneToOne
    @JoinColumn(name = "user_id")
    private Privacy privacy;
    @OneToMany(mappedBy = "users")
    private Set<Quote> quotes;
    @OneToMany(mappedBy = "users")
    private Set<Rating> ratings;

// TODO: Many to many
//    private Map<Bookshelf, Book> books;
//    private Set<User> friends;
}

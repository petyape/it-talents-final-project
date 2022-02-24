package com.example.goodreads.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    public enum Gender{
        MALE('m'), FEMALE('f'), CUSTOM('c'), NONE('n');

        public final char symbol;
        Gender(char symbol){
            this.symbol = symbol;
        }

        public static boolean isValidGender(char symbol) {
            return (symbol == NONE.symbol ||
                    symbol == MALE.symbol ||
                    symbol == FEMALE.symbol ||
                    symbol == CUSTOM.symbol);
        }
    }

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column
    private Boolean isAdmin;

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

    @Column
    private String photoUrl;

    @Column
    private char gender;

    @Column
    private String username;

    @Column
    private Boolean showLastName;

    @Column
    private Boolean isReverseNameOrder;

    @Column
    private char genderViewableBy;

    @Column
    private char locationViewableBy;

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

    @OneToOne
    @JoinColumn(name = "adress_id")
    private Address address;

    @OneToOne
    @JoinColumn(name = "privacy_id")
    private Privacy privacy;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private Set<Message> messagesSent;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private Set<Message> messagesReceived;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Review> reviews;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Quote> quotes;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Rating> ratings;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<UsersBooks> books;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_have_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<User> friends;

    @ManyToMany
    @JoinTable(
            name = "users_like_genres",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> favoriteGenres;

    @ManyToMany
    @JoinTable(
            name = "users_like_quotes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "quote_id"))
    private Set<Quote> favoriteQuotes;

}

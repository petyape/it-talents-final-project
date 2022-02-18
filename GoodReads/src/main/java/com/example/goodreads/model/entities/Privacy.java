package com.example.goodreads.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "privacy")
public class Privacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int privacyId;

    @Column
    private char viewProfile;

    @Column
    private Boolean canNonFriendsFollow;

    @Column
    private Boolean canNonFriendsComment;

    @Column
    private Boolean canDisplayReviews;

    @Column
    private Boolean privateMessages;

    @Column
    private Boolean isEmailVisible;

    @Column
    private Boolean allowSearchByEmail;

    @Column
    private String challengeQuestion;

    @Column
    private Boolean promptToRecommendBooks;

}

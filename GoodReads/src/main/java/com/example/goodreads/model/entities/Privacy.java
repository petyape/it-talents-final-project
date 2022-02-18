package com.example.goodreads.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "privacy")
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Privacy {

    public enum Visibility{EVERYONE('e'), FRIENDS('f'), NONE('n');

        public final char symbol;
        Visibility(char symbol){
            this.symbol = symbol;
        }

        public static boolean isValidVisibility(char symbol) {
            return (symbol == NONE.symbol ||
                    symbol == EVERYONE.symbol ||
                    symbol == FRIENDS.symbol);
        }
    }

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

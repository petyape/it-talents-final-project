package com.example.goodreads.model.dto;

import com.example.goodreads.model.entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PrivacyDTO {

    private User.Visibility viewProfile;
    private Boolean canNonFriendsFollow;
    private Boolean canNonFriendsComment;
    private Boolean canDisplayReviews;
    private Boolean privateMessages;
    private Boolean isEmailVisible;
    private Boolean allowSearchByEmail;
    private String challengeQuestion;
    private Boolean promptToRecommendBooks;


}

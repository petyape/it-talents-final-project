package com.example.goodreads.services;

import com.example.goodreads.model.entities.Privacy;
import com.example.goodreads.model.repository.PrivacyRepository;
import com.example.goodreads.services.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivacyService {

    @Autowired
    private PrivacyRepository privacyRepository;

    public Privacy createDefaultPrivacy(){
        Privacy pr = Privacy.builder()
                .canDisplayReviews(true)
                .allowSearchByEmail(true)
                .canNonFriendsComment(true)
                .canNonFriendsFollow(true)
                .challengeQuestion("")
                .isEmailVisible(true)
                .privateMessages(true)
                .promptToRecommendBooks(true)
                .viewProfile(Helper.Visibility.FRIENDS.symbol)
                .build();
        return privacyRepository.save(pr);
    }

}

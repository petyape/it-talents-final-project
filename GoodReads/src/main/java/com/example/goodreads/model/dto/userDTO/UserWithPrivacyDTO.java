package com.example.goodreads.model.dto.userDTO;

import com.example.goodreads.model.entities.Privacy;
import com.example.goodreads.services.util.Helper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserWithPrivacyDTO {

    private long userId;
    private Privacy privacy;

    public boolean isValid() {
        return (Helper.Visibility.isValidVisibility(privacy.getViewProfile()) &&
                privacy.getCanNonFriendsFollow() != null &&
                privacy.getCanNonFriendsComment() != null && privacy.getCanDisplayReviews() != null &&
                privacy.getPrivateMessages() != null && privacy.getIsEmailVisible() != null &&
                privacy.getAllowSearchByEmail() != null && privacy.getPromptToRecommendBooks() != null);
    }

}

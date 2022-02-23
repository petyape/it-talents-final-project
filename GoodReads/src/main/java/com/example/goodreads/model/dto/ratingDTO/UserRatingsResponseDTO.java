package com.example.goodreads.model.dto.ratingDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserRatingsResponseDTO {

    private long ratingId;
    private int rating;
    private String title;
    private long userId;
    private String firstName;

}

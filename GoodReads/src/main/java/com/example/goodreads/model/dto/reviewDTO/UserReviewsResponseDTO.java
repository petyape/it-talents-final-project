package com.example.goodreads.model.dto.reviewDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserReviewsResponseDTO {

    private long reviewId;
    private String review;
    private String title;
    private long userId;
    private String firstName;

}
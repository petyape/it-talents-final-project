package com.example.goodreads.model.dto.ratingDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RatingWithUserDTO {

    private long ratingId;
    private long userId;
    private String name;
    private int rating;

}

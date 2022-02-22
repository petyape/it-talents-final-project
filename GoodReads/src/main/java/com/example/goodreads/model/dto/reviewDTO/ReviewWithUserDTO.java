package com.example.goodreads.model.dto.reviewDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReviewWithUserDTO {

    private long reviewId;
    private long userId;
    private String name;
    private String review;

}

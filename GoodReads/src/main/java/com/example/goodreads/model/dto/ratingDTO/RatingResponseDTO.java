package com.example.goodreads.model.dto.ratingDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ResponseBody
public class RatingResponseDTO {

    private long ratingId;
    private long userId;
    private long bookId;
    private int rating;

}

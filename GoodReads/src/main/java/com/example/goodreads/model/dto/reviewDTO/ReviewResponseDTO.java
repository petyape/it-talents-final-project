package com.example.goodreads.model.dto.reviewDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ResponseBody
public class ReviewResponseDTO {

    private long reviewId;
    private long userId;
    private String title;
    private String review;
    private LocalDate reviewDate;

}

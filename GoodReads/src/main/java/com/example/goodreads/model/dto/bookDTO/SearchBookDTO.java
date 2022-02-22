package com.example.goodreads.model.dto.bookDTO;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchBookDTO {

    private long bookId;
    private String title;
    private List<String> authors;
    private double avgRating;
    private long ratings;
    private int published;
    private long editionsNumber;

}

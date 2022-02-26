package com.example.goodreads.model.dto.bookDTO;

import com.example.goodreads.model.dto.authorDTO.AuthorWithNameDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBookDTO {

    private long bookId;
    private String title;
    private String coverUrl;
    private String genreName;
    private String description;
    private List<AuthorWithNameDTO> authors;
    private double avgRating;
    private int ratingsNumber;
    private int reviewsNumber;
    private String language;
    private int pages;
    private String ISBN;
    private String originalTitle;
    private LocalDate publishDate;
    private String publisher;
    private int editionsNumber;

}

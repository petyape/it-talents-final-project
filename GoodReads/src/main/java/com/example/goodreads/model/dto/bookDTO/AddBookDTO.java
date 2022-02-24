package com.example.goodreads.model.dto.bookDTO;

import com.example.goodreads.model.dto.authorDTO.AuthorWithNameDTO;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddBookDTO {

    private String title;
    private String description;
    private int pages;
    private String isbn;
    private String originalTitle;
    private LocalDate publishDate;
    private String publisher;
    private long genreId;
    private long languageId;
    private AuthorWithNameDTO[] authorsWithName;

    public boolean isValid() {
        return (title != null && !title.isBlank() &&
                description != null && !description.isBlank() &&
                (pages >= 10) && isbn != null && !isbn.isBlank() &&
                originalTitle != null && !originalTitle.isBlank() &&
                publishDate != null && genreId > 0 && languageId > 0 &&
                authorsWithName != null);
    }

}

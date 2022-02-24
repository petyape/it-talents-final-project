package com.example.goodreads.model.dto.bookDTO;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookShelfResponseDTO {

    private long bookshelfId;
    private String name;
    private List<BookResponseDTO> booksPerUser;

}

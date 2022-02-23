package com.example.goodreads.model.dto.quoteDTO;

import com.example.goodreads.model.dto.authorDTO.AuthorWithNameDTO;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddQuoteDTO {

    private String quote;
    private String tags;
    private AuthorWithNameDTO author;

}

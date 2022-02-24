package com.example.goodreads.model.dto.quoteDTO;

import com.example.goodreads.model.dto.authorDTO.AuthorWithNameDTO;
import lombok.*;
import org.springframework.web.bind.annotation.ResponseBody;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ResponseBody
public class QuoteResponseDTO {

    private int quoteId;
    private String quote;
    private AuthorWithNameDTO author;

}

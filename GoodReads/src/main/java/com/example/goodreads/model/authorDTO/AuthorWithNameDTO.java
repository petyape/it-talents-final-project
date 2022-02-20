package com.example.goodreads.model.authorDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorWithNameDTO {

    private long authorId;
    private String authorName;

}

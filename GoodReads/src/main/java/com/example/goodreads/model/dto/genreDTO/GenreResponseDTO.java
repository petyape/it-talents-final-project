package com.example.goodreads.model.dto.genreDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ResponseBody
public class GenreResponseDTO {

    private long genreId;
    private String genreName;

}

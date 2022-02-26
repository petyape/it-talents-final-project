package com.example.goodreads.model.dto.bookDTO;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BookshelvesDTO {

    private List<BookResponseDTO> read = new ArrayList<>();
    private List<BookResponseDTO> wantToRead = new ArrayList<>();
    private List<BookResponseDTO> reading = new ArrayList<>();

    public void addReadBook(BookResponseDTO book) {
        read.add(book);
    }

    public void addWantToReadBook(BookResponseDTO book) {
        wantToRead.add(book);
    }

    public void addReadingBook(BookResponseDTO book) {
        reading.add(book);
    }
}

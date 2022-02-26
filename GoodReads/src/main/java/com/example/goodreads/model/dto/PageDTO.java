package com.example.goodreads.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@ResponseBody
@AllArgsConstructor
@Getter
public class PageDTO {
    public static final int maxElementsOnPage = 10;

    private int pageNumber;
    private int totalPages;
    private List pageList;

}

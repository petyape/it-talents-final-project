package com.example.goodreads.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ErrorDTO {

    private String msg;
    private int status;

}

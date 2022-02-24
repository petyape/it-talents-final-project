package com.example.goodreads.exceptions;

public class FileNotAllowedException extends RuntimeException {

    public FileNotAllowedException(String msg) {
        super(msg);
    }

}

package com.example.goodreads.exceptions;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String msg){
        super(msg);
    }
}

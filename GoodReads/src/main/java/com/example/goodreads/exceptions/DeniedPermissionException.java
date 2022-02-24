package com.example.goodreads.exceptions;

public class DeniedPermissionException extends RuntimeException {

    public DeniedPermissionException(String msg) {
        super(msg);
    }

}

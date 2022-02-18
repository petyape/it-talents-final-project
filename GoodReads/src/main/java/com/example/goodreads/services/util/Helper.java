package com.example.goodreads.services.util;

public class Helper {
    public static Boolean isValidEmail(String email) {
        return (email != null && email.matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"));
    }
}

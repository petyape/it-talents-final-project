package com.example.goodreads.services.util;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.FileNotAllowedException;
import com.example.goodreads.model.dto.PageDTO;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class Helper {

    public enum Visibility{EVERYONE('e'), FRIENDS('f'), NONE('n');

        public final char symbol;
        Visibility(char symbol){
            this.symbol = symbol;
        }

        public static boolean isValidVisibility(char symbol) {
            return (symbol == NONE.symbol ||
                    symbol == EVERYONE.symbol ||
                    symbol == FRIENDS.symbol);
        }

        public static Visibility getVisibility(char symbol) {
            if (symbol == 'e') {return EVERYONE;}
            if (symbol == 'f') {return FRIENDS;}
            return NONE;
        }
    }

    public static Boolean isValidEmail(String email) {
        if (charCounter(email) == 1) {
            return EmailValidator.getInstance()
                    .isValid(email);
        }
        return false;
    }

    public static void validatePassword(String password) {
        if (!password.matches("(?=^.{8,}$)(?=.*\\d)(?=.*[!@#$%^&*]+)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) {
            throw new BadRequestException("Password must contain at least one lower case letter, " +
                    "one upper case letter, one number, one special character " +
                    "and should be at least 8 characters long.");
        }
    }

    public static int charCounter(String text) {
        int counter = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '@') {
                counter++;
            }
        }
        return counter;
    }

    public static void validateFile(MultipartFile multipartFile) {
        boolean result = true;
        String contentType = multipartFile.getContentType();
        if (contentType == null || !isSupportedContentType(contentType)) {
            throw new FileNotAllowedException("Only PNG, JPG or JPEG images allowed!");
        }
    }

    private static boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }

    public static PageDTO createPage(List<?> objList, int page) {
        PagedListHolder<List<?>> pageHolder = new PagedListHolder(objList);
        pageHolder.setPageSize(PageDTO.maxElementsOnPage); // number of items per page
        if (page > pageHolder.getPageCount()) {
            throw new BadRequestException("Searched page does not exist!");
        }
        pageHolder.setPage(page - 1);
        return new PageDTO(page, pageHolder.getPageCount(), pageHolder.getPageList());
    }

    public static void validatePage(int page) {
        if  (page <= 0) {
            throw new BadRequestException("Invalid page parameter!");
        }
    }

}

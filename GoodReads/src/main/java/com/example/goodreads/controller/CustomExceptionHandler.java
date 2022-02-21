package com.example.goodreads.controller;

import com.example.goodreads.exceptions.BadRequestException;
import com.example.goodreads.exceptions.DeniedPermissionException;
import com.example.goodreads.exceptions.NotFoundException;
import com.example.goodreads.exceptions.UnauthorizedException;
import com.example.goodreads.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value ={UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorDTO handleUnauthorized(Exception e){
        ErrorDTO dto = new ErrorDTO();
        dto.setMsg(e.getMessage());
        dto.setStatus(HttpStatus.UNAUTHORIZED.value());
        return dto;
    }

    @ExceptionHandler(value ={BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleBadRequest(Exception e){
        ErrorDTO dto = new ErrorDTO();
        dto.setMsg(e.getMessage());
        dto.setStatus(HttpStatus.BAD_REQUEST.value());
        return dto;
    }

    @ExceptionHandler(value ={DeniedPermissionException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ErrorDTO handleDeniedPermission(Exception e){
        ErrorDTO dto = new ErrorDTO();
        dto.setMsg(e.getMessage());
        dto.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        return dto;
    }

    @ExceptionHandler(value ={NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleNotFound(Exception e){
        ErrorDTO dto = new ErrorDTO();
        dto.setMsg(e.getMessage());
        dto.setStatus(HttpStatus.NOT_FOUND.value());
        return dto;
    }

    // TODO: Global exception handler!!!
}

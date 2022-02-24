package com.example.goodreads.controller;

import com.example.goodreads.exceptions.*;
import com.example.goodreads.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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

    @ExceptionHandler(value ={FileNotAllowedException.class})
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public ErrorDTO handleFileNotAllowed(Exception e){
        ErrorDTO dto = new ErrorDTO();
        dto.setMsg(e.getMessage());
        dto.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        return dto;
    }

    @ExceptionHandler(value ={MaxUploadSizeExceededException.class})
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ResponseBody
    public ErrorDTO handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e){
        ErrorDTO dto = new ErrorDTO();
        dto.setMsg("File is too big!");
        dto.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
        return dto;
    }

    @ExceptionHandler(value ={Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleException(Exception e){
        ErrorDTO dto = new ErrorDTO();
        dto.setMsg("Sorry, an unexpected error occurred!");
        dto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return dto;
    }

}

package com.project.raif.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiException.class)
    public ErrorResponseDto onApiException(ApiException ex) {
        return new ErrorResponseDto(ex.getCode().getCode(), ex.getCode().getMessage());
    }
}

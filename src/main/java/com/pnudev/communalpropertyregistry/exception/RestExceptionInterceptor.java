package com.pnudev.communalpropertyregistry.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionInterceptor {

//    @ExceptionHandler(Exception.class)
    public ErrorResponse handleInternalServerError(Exception exception) {
        return ErrorResponse.builder()
                .message(exception.getMessage())
                .status(500)
                .build();
    }

    @ExceptionHandler(ServiceApiException.class)
    public ErrorResponse handleServiceApiException(ServiceApiException serviceApiException) {
        return ErrorResponse.builder()
                .message(serviceApiException.getMessage())
                .status(404)
                .build();
    }
}

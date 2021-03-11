package com.pnudev.communalpropertyregistry.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionInterceptor {

//    @ExceptionHandler(Exception.class)
//    public ErrorResponse generalError() {
//        return ErrorResponse.builder()
//                .message("Внутрішня помилка сервера")
//                .status(500)
//                .build();
//    }

    @ExceptionHandler(ServiceApiException.class)
    public ErrorResponse serviceApiException(ServiceApiException serviceApiException) {
        return ErrorResponse.builder()
                .message(serviceApiException.getMessage())
                .status(400)
                .build();
    }
}

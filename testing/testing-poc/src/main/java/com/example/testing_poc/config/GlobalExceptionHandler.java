package com.example.testing_poc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(
            ApiException exception) {

        return ResponseEntity
                .status(exception.getStatus())
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .errorCode(exception.getErrorCode())
                                .detail(
                                        messageSource.getMessage(
                                                exception.getErrorCode(),
                                                exception.getErrorArgs(),
                                                exception.getErrorCode(),
                                                LocaleContextHolder.getLocale()
                                        )
                                )
                                .build()
                );
    }
}
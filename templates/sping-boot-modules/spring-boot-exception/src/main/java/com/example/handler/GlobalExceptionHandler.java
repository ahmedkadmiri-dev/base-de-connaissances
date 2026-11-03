package com.example.handler;

import com.example.exception.ApiException;
import com.example.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(
            ApiException exception) {

        return ResponseEntity
                .status(exception.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .errorCode(exception.getErrorCode())
                                .message(
                                        messageSource.getMessage(
                                                exception.getErrorCode(),
                                                exception.getErrorArgs(),
                                                exception.getErrorCode(),
                                                LocaleContextHolder.getLocale()
                                        )
                                )
                                .details(exception.getDetails())
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception) {

        log.error("Unexpected error occurred", exception);

        return ResponseEntity.internalServerError()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .errorCode("INTERNAL_SERVER_ERROR")
                                .message(
                                        messageSource.getMessage(
                                                "INTERNAL_SERVER_ERROR",
                                                null,
                                                "INTERNAL_SERVER_ERROR",
                                                LocaleContextHolder.getLocale()
                                        )
                                )
                                .build()
                );
    }
}
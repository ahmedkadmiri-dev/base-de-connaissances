package com.example.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    private final String errorCode;

    private final Object[] errorArgs;

    private final Map<String, Object> details;
}
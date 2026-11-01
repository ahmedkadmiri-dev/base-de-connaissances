package com.example.testing_poc.config.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    private final String errorCode;

    private final Object[] errorArgs;

    private final Map<String, Object> details;

    public ApiException(
            HttpStatus status,
            String errorCode,
            Object[] errorArgs,
            Map<String, Object> details) {

        super(errorCode);
        this.status = status;
        this.errorCode = errorCode;
        this.errorArgs = errorArgs;
        this.details = details != null ? details : Collections.emptyMap();
    }
}
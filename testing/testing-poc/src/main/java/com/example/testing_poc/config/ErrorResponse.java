package com.example.testing_poc.config;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private LocalDateTime timestamp;

    private String errorCode;

    private String detail;
}
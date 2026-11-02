package com.example.model;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse {

    private LocalDateTime timestamp;

    private String errorCode;

    private String message;

    private Map<String, Object> details;

}
package com.example.utils;

import org.springframework.stereotype.Component;

@Component
public class LoggingSanitizer {

    public String sanitize(String value) {
        if (value == null) {
            return null;
        }

        return value
                .replaceAll("(?i)(password|token|secret)=([^&\\s]+)", "$1=******")
                .replaceAll("(?i)(authorization):\\s*([^\\s]+)", "$1: ******");
    }

    public String sanitize(Object value, int maxLength) {
        if (value == null) {
            return "null";
        }

        String sanitized = sanitize(String.valueOf(value));

        return sanitized.length() > maxLength
                ? sanitized.substring(0, maxLength) + "..."
                : sanitized;
    }

    public String sanitize(Object[] values, int maxLength) {
        if (values == null) {
            return "[]";
        }

        return sanitize(java.util.Arrays.toString(values), maxLength);
    }
}
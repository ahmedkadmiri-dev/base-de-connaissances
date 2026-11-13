package com.example.audit.models;

public record AuditContext(
        String actorId,
        String source,
        String traceId
) {
}
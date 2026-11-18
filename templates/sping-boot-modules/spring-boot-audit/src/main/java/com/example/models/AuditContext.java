package com.example.models;

public record AuditContext(
        String actorId,
        String actorUsername,
        String source,
        String traceId,
        String idempotencyKey
) {
}
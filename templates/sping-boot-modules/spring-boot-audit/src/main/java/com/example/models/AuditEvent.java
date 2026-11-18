package com.example.models;

import com.example.enums.AuditAction;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import com.example.enums.AuditStatus;
import lombok.Builder;

@Builder
public record AuditEvent(
        String aggregateType,
        String aggregateId,
        AuditAction action,
        String actorId,
        String actorName,
        String source,
        String traceId,
        String idempotencyKey,
        Map<String, Object> before,
        Map<String, Object> after,
        Instant occurredAt,
        AuditStatus status
) {
}
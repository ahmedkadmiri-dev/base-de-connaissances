package com.example.audit.models;

import com.example.audit.enums.AuditAction;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;

@Builder
public record AuditEvent(
        UUID eventId,
        String aggregateType,
        String aggregateId,
        AuditAction action,
        String actorId,
        String source,
        String traceId,
        Map<String, Object> before,
        Map<String, Object> after,
        Instant occurredAt
) {
}

package com.example.services.impl;

import com.example.entities.AuditEventEntity;
import com.example.models.AuditCommand;
import com.example.models.AuditContext;
import com.example.properties.PayloadProperties;
import com.example.providers.AuditContextProvider;
import com.example.repositories.AuditEventRepository;
import com.example.services.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.PayloadTooLargeException;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditEventRepository repository;
    private final AuditContextProvider contextProvider;
    private final ObjectMapper objectMapper;
    private final PayloadProperties payloadProperties;

    @Override
    public void record(AuditCommand command) {

        AuditContext context = contextProvider.getCurrentContext();

        AuditEventEntity entity = new AuditEventEntity();

        entity.setAggregateType(command.aggregateType());
        entity.setAggregateId(command.aggregateId());
        entity.setAction(command.action().name());
        entity.setClientIpAddress(context.clientIpAddress());
        entity.setCallerIpAddress(context.callerIpAddress());
        entity.setActorId(context.actorId());
        entity.setActorUsername(context.actorUsername());
        entity.setSource(context.source());
        entity.setTraceId(context.traceId());
        entity.setIdempotencyKey(context.idempotencyKey());
        entity.setBeforeState(toJson(command.before()));
        entity.setAfterState(toJson(command.after()));
        entity.setOccurredAt(Instant.now());
        entity.setStatus(command.status());

        repository.save(entity);
    }

    private String toJson(Object data) {
        if (data == null) {
            return null;
        }

        try {
            String json = objectMapper.writeValueAsString(data);

            byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
            int maxSize = payloadProperties.getMaxSize();

            if (jsonBytes.length <= maxSize) {
                return json;
            }

            // Troncature UTF-8 sans couper un caractère multioctet
            int end = maxSize;

            while (end > 0 && (jsonBytes[end] & 0xC0) == 0x80) {
                end--;
            }

            return new String(jsonBytes, 0, end, StandardCharsets.UTF_8);

        } catch (Exception exception) {
            return null;
        }
    }
}

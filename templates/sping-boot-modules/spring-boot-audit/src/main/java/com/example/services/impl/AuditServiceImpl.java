package com.example.services.impl;

import com.example.models.AuditCommand;
import com.example.models.AuditContext;
import com.example.providers.AuditContextProvider;
import com.example.entities.AuditEventEntity;
import com.example.repositories.AuditEventRepository;
import com.example.services.AuditService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditEventRepository repository;
    private final AuditContextProvider contextProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void record(AuditCommand command) {

        AuditContext context = contextProvider.getCurrentContext();

        AuditEventEntity entity = new AuditEventEntity();

        entity.setAggregateType(command.aggregateType());
        entity.setAggregateId(command.aggregateId());
        entity.setAction(command.action().name());

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

    private String toJson(Map<String, Object> data) {
        if (data == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to serialize audit data", exception);
        }
    }
}
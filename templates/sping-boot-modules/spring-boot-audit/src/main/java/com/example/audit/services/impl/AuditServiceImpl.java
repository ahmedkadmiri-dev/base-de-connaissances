package com.example.audit.services.impl;

import com.example.audit.config.AuditContextProvider;
import com.example.audit.models.*;
import com.example.audit.entities.AuditEventEntity;
import com.example.audit.repositories.AuditEventRepository;
import com.example.audit.services.AuditService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditEventRepository repository;
    private final AuditContextProvider contextProvider;
    private final ModelMapper modelMapper;

    @Override
    public void record(AuditCommand command) {

        AuditContext context = contextProvider.getCurrentContext();

        AuditEvent event = AuditEvent.builder()
                .eventId(UUID.randomUUID())
                .aggregateType(command.aggregateType())
                .aggregateId(command.aggregateId())
                .action(command.action())
                .actorId(context.actorId())
                .source(context.source())
                .traceId(context.traceId())
                .before(command.before())
                .after(command.after())
                .occurredAt(Instant.now())
                .build();

        AuditEventEntity entity = modelMapper.map(event, AuditEventEntity.class);

        repository.save(entity);
    }
}
package com.example.services.impl;

import com.example.models.AuditCommand;
import com.example.models.AuditContext;
import com.example.models.AuditEvent;
import com.example.providers.AuditContextProvider;
import com.example.entities.AuditEventEntity;
import com.example.repositories.AuditEventRepository;
import com.example.services.AuditService;
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
                .aggregateType(command.aggregateType())
                .aggregateId(command.aggregateId())
                .action(command.action())
                .actorId(context.actorId())
                .actorName(context.actorUsername())
                .source(context.source())
                .traceId(context.traceId())
                .idempotencyKey(context.idempotencyKey())
                .before(command.before())
                .after(command.after())
                .occurredAt(Instant.now())
                .status(command.status())
                .build();

        AuditEventEntity entity = modelMapper.map(event, AuditEventEntity.class);

        repository.save(entity);
    }
}
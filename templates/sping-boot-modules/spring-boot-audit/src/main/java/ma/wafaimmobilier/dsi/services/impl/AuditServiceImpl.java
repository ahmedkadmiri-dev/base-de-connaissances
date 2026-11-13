package ma.wafaimmobilier.dsi.services.impl;

import ma.wafaimmobilier.dsi.config.AuditContextProvider;
import com.example.audit.models.*;
import ma.wafaimmobilier.dsi.entities.AuditEventEntity;
import ma.wafaimmobilier.dsi.models.AuditCommand;
import ma.wafaimmobilier.dsi.models.AuditContext;
import ma.wafaimmobilier.dsi.models.AuditEvent;
import ma.wafaimmobilier.dsi.repositories.AuditEventRepository;
import ma.wafaimmobilier.dsi.services.AuditService;
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
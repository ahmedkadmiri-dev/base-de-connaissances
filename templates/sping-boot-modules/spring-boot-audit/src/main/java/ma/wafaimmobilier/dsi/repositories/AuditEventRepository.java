package ma.wafaimmobilier.dsi.repositories;

import ma.wafaimmobilier.dsi.entities.AuditEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AuditEventRepository extends JpaRepository<AuditEventEntity, UUID> {

    List<AuditEventEntity> findByAggregateTypeAndAggregateIdOrderByOccurredAtDesc(
            String aggregateType,
            String aggregateId
    );

    List<AuditEventEntity> findByActorIdOrderByOccurredAtDesc(String actorId);

    List<AuditEventEntity> findByCorrelationIdOrderByOccurredAtDesc(String correlationId);
}
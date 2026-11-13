package ma.wafaimmobilier.dsi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "AUDIT_EVENT")
public class AuditEventEntity {

    @Id
    @Column(name = "EVENT_ID", nullable = false, updatable = false)
    private UUID eventId;

    @Column(name = "AGGREGATE_TYPE", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "AGGREGATE_ID", nullable = false, length = 100)
    private String aggregateId;

    @Column(name = "ACTION", nullable = false, length = 50)
    private String action;

    @Column(name = "ACTOR_ID", length = 150)
    private String actorId;

    @Column(name = "SOURCE", length = 100)
    private String source;

    @Column(name = "CORRELATION_ID", length = 150)
    private String correlationId;

    @Lob
    @Column(name = "BEFORE_STATE")
    private String beforeState;

    @Lob
    @Column(name = "AFTER_STATE")
    private String afterState;

    @Column(name = "OCCURRED_AT", nullable = false, updatable = false)
    private Instant occurredAt;
}
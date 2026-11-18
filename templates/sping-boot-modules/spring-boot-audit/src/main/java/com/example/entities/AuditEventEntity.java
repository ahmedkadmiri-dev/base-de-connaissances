package com.example.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AUDIT_EVENT")
public class AuditEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_event_seq")
    @SequenceGenerator(
            name = "audit_event_seq",
            sequenceName = "SEQ_AUDIT_EVENT",
            allocationSize = 1
    )
    @Column(name = "EVENT_ID", nullable = false, updatable = false)
    private Long eventId;

    @Column(name = "AGGREGATE_TYPE", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "AGGREGATE_ID", nullable = false, length = 100)
    private String aggregateId;

    @Column(name = "ACTION", nullable = false, length = 50)
    private String action;

    @Column(name = "ACTOR_ID", length = 150)
    private String actorId;

    @Column(name = "ACTOR_USERNAME", length = 150)
    private String actorUsername;

    @Column(name = "SOURCE", length = 100)
    private String source;

    @Column(name = "TRACE_ID", length = 150)
    private String traceId;

    @Column(name = "IDEMPOTENCY_KEY", length = 100)
    private String idempotencyKey;

    @Lob
    @Column(name = "BEFORE_STATE")
    private String beforeState;

    @Lob
    @Column(name = "AFTER_STATE")
    private String afterState;

    @Column(name = "OCCURRED_AT", nullable = false, updatable = false)
    private Instant occurredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private AuditStatus status;
}
package com.example.models;

import com.example.enums.AuditAction;
import com.example.enums.AuditStatus;
import java.util.Map;

public record AuditCommand(

    String aggregateType,

    String aggregateId,

    AuditAction action,

    Object before,

    Object after,

    AuditStatus status

) {}

package com.example.audit.models;

import com.example.audit.enums.AuditAction;

import java.util.Map;

public record AuditCommand(

    String aggregateType,

    String aggregateId,

    AuditAction action,

    Map<String, Object> before,

    Map<String, Object> after

) {}

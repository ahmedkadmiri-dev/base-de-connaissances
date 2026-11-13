package ma.wafaimmobilier.dsi.models;

import ma.wafaimmobilier.dsi.enums.AuditAction;

import java.util.Map;

public record AuditCommand(

    String aggregateType,

    String aggregateId,

    AuditAction action,

    Map<String, Object> before,

    Map<String, Object> after

) {}

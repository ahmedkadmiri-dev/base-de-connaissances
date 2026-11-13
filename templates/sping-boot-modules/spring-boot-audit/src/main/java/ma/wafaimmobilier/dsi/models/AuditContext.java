package ma.wafaimmobilier.dsi.models;

public record AuditContext(
        String actorId,
        String source,
        String traceId
) {
}
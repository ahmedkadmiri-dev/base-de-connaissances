package ma.wafaimmobilier.dsi.services;

import ma.wafaimmobilier.dsi.models.AuditCommand;

public interface AuditService {

    void record(AuditCommand command);
}
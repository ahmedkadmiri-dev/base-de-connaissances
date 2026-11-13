package com.example.audit.services;

import com.example.audit.models.AuditCommand;

public interface AuditService {

    void record(AuditCommand command);
}
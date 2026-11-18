package com.example.services;

import com.example.models.AuditCommand;

public interface AuditService {

    void record(AuditCommand command);
}
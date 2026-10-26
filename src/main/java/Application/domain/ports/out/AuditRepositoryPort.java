package Application.domain.ports.out;

import Application.domain.model.AuditLog;

// registra eventos de auditoria (se implementa sobre MongoDB)
public interface AuditRepositoryPort {
    AuditLog log(AuditLog entry);
}

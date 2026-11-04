package Application.adapters.persistence.mongodb;

import Application.domain.model.AuditLog;

public final class AuditLogMapper {

    private AuditLogMapper() {
    }

    public static AuditLog toDomain(AuditLogDocument document) {
        if (document == null) {
            return null;
        }
        AuditLog entry = new AuditLog(document.getEventType(), document.getActorEmail(), document.getActorRole(),
                document.getDescription());
        entry.assignId(document.getId());
        // el constructor pone la fecha actual; se restaura la fecha real
        // guardada (ver AuditLog.assignOccurredAt)
        entry.assignOccurredAt(document.getOccurredAt());
        return entry;
    }

    public static AuditLogDocument toDocument(AuditLog entry) {
        return new AuditLogDocument(entry.getId(), entry.getEventType(), entry.getActorEmail(),
                entry.getActorRole(), entry.getDescription(), entry.getOccurredAt());
    }
}

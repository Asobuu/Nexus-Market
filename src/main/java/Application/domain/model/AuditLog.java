package Application.domain.model;

import Application.domain.enums.AuditEventType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// un evento que queda registrado para poder rastrear despues que paso
// y quien lo hizo (se guarda en MongoDB, no en MySQL)
@Getter
@NoArgsConstructor
public class AuditLog {

    private String id;
    private AuditEventType eventType;
    private String actorEmail;
    private String actorRole;
    private String description;
    private LocalDateTime occurredAt;

    public AuditLog(AuditEventType eventType, String actorEmail, String actorRole, String description) {
        this.eventType = eventType;
        this.actorEmail = actorEmail;
        this.actorRole = actorRole;
        this.description = description;
        this.occurredAt = LocalDateTime.now();
    }

    public void assignId(String id) {
        this.id = id;
    }
}

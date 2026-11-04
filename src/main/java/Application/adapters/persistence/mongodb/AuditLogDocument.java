package Application.adapters.persistence.mongodb;

import Application.domain.enums.AuditEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDocument {

    @Id
    private String id;
    private AuditEventType eventType;
    private String actorEmail;
    private String actorRole;
    private String description;
    private LocalDateTime occurredAt;
}

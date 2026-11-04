package Application.adapters.persistence.mongodb;

import Application.domain.model.AuditLog;
import Application.domain.ports.out.AuditRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMongoAdapter implements AuditRepositoryPort {

    private final AuditLogMongoRepository auditLogMongoRepository;

    public AuditLogMongoAdapter(AuditLogMongoRepository auditLogMongoRepository) {
        this.auditLogMongoRepository = auditLogMongoRepository;
    }

    @Override
    public AuditLog log(AuditLog entry) {
        AuditLogDocument document = auditLogMongoRepository.save(AuditLogMapper.toDocument(entry));
        return AuditLogMapper.toDomain(document);
    }
}

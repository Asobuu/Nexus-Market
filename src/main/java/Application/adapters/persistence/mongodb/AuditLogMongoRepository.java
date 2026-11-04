package Application.adapters.persistence.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String> {
}

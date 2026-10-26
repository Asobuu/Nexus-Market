# SDD — Adaptadores de Persistencia

## Relacional (MySQL, `adapters/persistence/jpa/`)

Por cada modelo de dominio existe una clase espejo anotada con `@Entity` en este paquete (por ejemplo `domain.model.Product` → `adapters.persistence.jpa.entity.ProductJpaEntity`). Un `Mapper` estático convierte en ambas direcciones. Cada `JpaRepository` (Spring Data) se envuelve en un `Adapter` que implementa el puerto de salida correspondiente:

```
ProductJpaEntity (@Entity)
ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long>
ProductMapper        // toDomain(entity) / toEntity(domain)
ProductRepositoryAdapter implements ProductRepositoryPort
```

Se repite igual para cada entidad listada en `Domain-Model.md`.

## Auditoría (MongoDB, `adapters/persistence/mongodb/`)

```
AuditLogDocument (@Document(collection = "audit_logs"))
AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String>
AuditLogMapper
AuditLogMongoAdapter implements AuditRepositoryPort
```

Base de datos: `nexusmarket_audit_db`, colección `audit_logs`. Se crea sola al insertar el primer evento (comportamiento por defecto de Mongo).

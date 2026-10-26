# SDD — Puertos de Salida (Output Ports)

Viven en `domain/ports/out/`. El dominio depende de estas interfaces, nunca de JPA/Mongo directamente. Las implementan los adaptadores de persistencia.

## Repositorios (uno por entidad raíz)

```java
UserRepositoryPort        // findById, existsByEmail, save
BuyerRepositoryPort
SellerRepositoryPort
LogisticsOperatorRepositoryPort
WarehouseRepositoryPort
ProductRepositoryPort     // findAll, findById, save
ProductVariantRepositoryPort
InventoryRepositoryPort
InventoryMovementRepositoryPort
CartRepositoryPort
OrderRepositoryPort
InvoiceRepositoryPort
ShipmentRepositoryPort
ReturnRepositoryPort
```

Todas siguen la misma forma mínima, por ejemplo:

```java
public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
}
```

`UserRepositoryPort` agrega:

```java
boolean existsByEmail(String email);
```

## Servicios de soporte

```java
PasswordServicePort  // hash(rawPassword), matches(rawPassword, hash)
JwtServicePort        // generateToken(user), validateToken(token), getUserIdFromToken(token)
AuditRepositoryPort   // log(AuditLog entry)  -> implementado sobre MongoDB
```

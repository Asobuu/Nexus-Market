package Application.adapters.persistence.jpa.repository;

import Application.adapters.persistence.jpa.entity.InventoryMovementJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryMovementJpaRepository extends JpaRepository<InventoryMovementJpaEntity, Long> {
}

package Application.adapters.persistence.jpa.repository;

import Application.adapters.persistence.jpa.entity.WarehouseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseJpaRepository extends JpaRepository<WarehouseJpaEntity, Long> {
}

package Application.adapters.persistence.jpa.repository;

import Application.adapters.persistence.jpa.entity.InventoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryJpaRepository extends JpaRepository<InventoryJpaEntity, Long> {
    List<InventoryJpaEntity> findByProductId(Long productId);
}

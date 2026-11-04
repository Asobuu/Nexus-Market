package Application.adapters.persistence.jpa.repository;

import Application.adapters.persistence.jpa.entity.ShipmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentJpaRepository extends JpaRepository<ShipmentJpaEntity, Long> {
    List<ShipmentJpaEntity> findByOrderId(Long orderId);
}

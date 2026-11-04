package Application.adapters.persistence.jpa.repository;

import Application.adapters.persistence.jpa.entity.CartJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartJpaRepository extends JpaRepository<CartJpaEntity, Long> {
    Optional<CartJpaEntity> findByBuyerId(Long buyerId);
}

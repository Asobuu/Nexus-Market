package Application.adapters.persistence.jpa.repository;

import Application.adapters.persistence.jpa.entity.SellerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerJpaRepository extends JpaRepository<SellerJpaEntity, Long> {
}

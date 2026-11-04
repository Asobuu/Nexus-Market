package Application.adapters.persistence.jpa.repository;

import Application.adapters.persistence.jpa.entity.BuyerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerJpaRepository extends JpaRepository<BuyerJpaEntity, Long> {
}

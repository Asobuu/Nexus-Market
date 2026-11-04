package Application.adapters.persistence.jpa.repository;

import Application.adapters.persistence.jpa.entity.LogisticsOperatorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogisticsOperatorJpaRepository extends JpaRepository<LogisticsOperatorJpaEntity, Long> {
}

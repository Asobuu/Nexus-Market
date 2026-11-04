package Application.adapters.persistence.jpa.repository;

import Application.adapters.persistence.jpa.entity.ReturnJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnJpaRepository extends JpaRepository<ReturnJpaEntity, Long> {
}

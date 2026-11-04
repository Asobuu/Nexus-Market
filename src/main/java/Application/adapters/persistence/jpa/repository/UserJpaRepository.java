package Application.adapters.persistence.jpa.repository;

import Application.adapters.persistence.jpa.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    boolean existsByEmail(String email);
    Optional<UserJpaEntity> findByEmail(String email);
}

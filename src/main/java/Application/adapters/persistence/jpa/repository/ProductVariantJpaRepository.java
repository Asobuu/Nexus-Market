package Application.adapters.persistence.jpa.repository;

import Application.adapters.persistence.jpa.entity.ProductVariantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantJpaRepository extends JpaRepository<ProductVariantJpaEntity, Long> {
}

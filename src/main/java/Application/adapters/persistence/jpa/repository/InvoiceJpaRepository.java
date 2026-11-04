package Application.adapters.persistence.jpa.repository;

import Application.adapters.persistence.jpa.entity.InvoiceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceJpaEntity, Long> {
}

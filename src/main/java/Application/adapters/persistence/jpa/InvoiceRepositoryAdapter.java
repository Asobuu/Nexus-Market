package Application.adapters.persistence.jpa;

import Application.adapters.persistence.jpa.entity.InvoiceJpaEntity;
import Application.adapters.persistence.jpa.entity.OrderJpaEntity;
import Application.adapters.persistence.jpa.mapper.InvoiceMapper;
import Application.adapters.persistence.jpa.repository.InvoiceJpaRepository;
import Application.adapters.persistence.jpa.repository.OrderJpaRepository;
import Application.domain.model.Invoice;
import Application.domain.ports.out.InvoiceRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceRepositoryAdapter implements InvoiceRepositoryPort {

    private final InvoiceJpaRepository invoiceJpaRepository;
    private final OrderJpaRepository orderJpaRepository;

    public InvoiceRepositoryAdapter(InvoiceJpaRepository invoiceJpaRepository,
                                     OrderJpaRepository orderJpaRepository) {
        this.invoiceJpaRepository = invoiceJpaRepository;
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Invoice save(Invoice invoice) {
        OrderJpaEntity orderEntity = orderJpaRepository.getReferenceById(invoice.getOrder().getId());
        InvoiceJpaEntity entity = invoiceJpaRepository.save(InvoiceMapper.toEntity(invoice, orderEntity));
        return InvoiceMapper.toDomain(entity);
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceJpaRepository.findAll().stream().map(InvoiceMapper::toDomain).toList();
    }
}

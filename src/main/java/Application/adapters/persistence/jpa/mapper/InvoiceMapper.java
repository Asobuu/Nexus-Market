package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.InvoiceJpaEntity;
import Application.adapters.persistence.jpa.entity.OrderJpaEntity;
import Application.domain.model.Invoice;

public final class InvoiceMapper {

    private InvoiceMapper() {
    }

    public static Invoice toDomain(InvoiceJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        Invoice invoice = new Invoice(OrderMapper.toDomain(entity.getOrder()));
        invoice.assignId(entity.getId());
        // el constructor recalcula el total desde el pedido; se restaura el
        // monto real que quedo grabado (ver Invoice.assignTotalAmount)
        invoice.assignTotalAmount(entity.getTotalAmount());
        return invoice;
    }

    public static InvoiceJpaEntity toEntity(Invoice invoice, OrderJpaEntity orderEntity) {
        InvoiceJpaEntity entity = new InvoiceJpaEntity(orderEntity, invoice.getTotalAmount());
        entity.setId(invoice.getId());
        return entity;
    }
}

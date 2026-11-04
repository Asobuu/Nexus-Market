package Application.domain.services;

import Application.domain.enums.AuditEventType;
import Application.domain.model.AuditLog;
import Application.domain.model.Invoice;
import Application.domain.model.Order;
import Application.domain.ports.out.AuditRepositoryPort;
import Application.domain.ports.out.InvoiceRepositoryPort;
import Application.domain.ports.out.OrderRepositoryPort;

// todo lo relacionado a cobrar un pedido: marcarlo pagado y generar su
// factura. antes esto vivia dentro de OrderService mezclado con la
// logica del pedido; el profesor marco que son responsabilidades
// distintas (una cosa es el pedido en si, otra es cobrarlo) asi que
// quedo en su propia clase
public class PaymentService {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final AuditRepositoryPort auditRepositoryPort;

    public PaymentService(OrderRepositoryPort orderRepositoryPort, InvoiceRepositoryPort invoiceRepositoryPort,
                           AuditRepositoryPort auditRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.invoiceRepositoryPort = invoiceRepositoryPort;
        this.auditRepositoryPort = auditRepositoryPort;
    }

    public Invoice payOrder(Order order) {
        order.pay();
        orderRepositoryPort.save(order);

        Invoice invoice = new Invoice(order);
        invoice = invoiceRepositoryPort.save(invoice);

        auditRepositoryPort.log(new AuditLog(AuditEventType.ORDER_PAID, order.getBuyer().getEmail(), "BUYER",
                "se pago el pedido " + order.getId()));
        return invoice;
    }
}

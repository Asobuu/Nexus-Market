package Application.domain.services;

import Application.domain.enums.AuditEventType;
import Application.domain.model.AuditLog;
import Application.domain.model.Invoice;
import Application.domain.model.Order;
import Application.domain.model.Shipment;
import Application.domain.model.Warehouse;
import Application.domain.ports.out.AuditRepositoryPort;
import Application.domain.ports.out.InvoiceRepositoryPort;
import Application.domain.ports.out.OrderRepositoryPort;
import Application.domain.ports.out.ShipmentRepositoryPort;

// ciclo de vida del pedido: pagar, generar el envio y entregarlo
public class OrderService {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final ShipmentRepositoryPort shipmentRepositoryPort;
    private final AuditRepositoryPort auditRepositoryPort;

    public OrderService(OrderRepositoryPort orderRepositoryPort, InvoiceRepositoryPort invoiceRepositoryPort,
                         ShipmentRepositoryPort shipmentRepositoryPort, AuditRepositoryPort auditRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.invoiceRepositoryPort = invoiceRepositoryPort;
        this.shipmentRepositoryPort = shipmentRepositoryPort;
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

    public Shipment shipOrder(Order order, Warehouse originWarehouse) {
        Shipment shipment = new Shipment(order, originWarehouse);
        shipment.ship();
        shipmentRepositoryPort.save(shipment);

        order.ship();
        orderRepositoryPort.save(order);

        auditRepositoryPort.log(new AuditLog(AuditEventType.ORDER_SHIPPED, order.getBuyer().getEmail(), "BUYER",
                "se despacho el pedido " + order.getId()));
        return shipment;
    }

    public void deliverOrder(Order order, Shipment shipment) {
        shipment.deliver();
        shipmentRepositoryPort.save(shipment);

        order.deliver();
        orderRepositoryPort.save(order);

        auditRepositoryPort.log(new AuditLog(AuditEventType.ORDER_DELIVERED, order.getBuyer().getEmail(), "BUYER",
                "se entrego el pedido " + order.getId()));
    }
}

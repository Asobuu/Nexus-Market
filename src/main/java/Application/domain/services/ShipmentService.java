package Application.domain.services;

import Application.domain.enums.AuditEventType;
import Application.domain.enums.ShipmentStatus;
import Application.domain.model.AuditLog;
import Application.domain.model.Order;
import Application.domain.model.Shipment;
import Application.domain.model.user.LogisticsOperator;
import Application.domain.ports.out.AuditRepositoryPort;
import Application.domain.ports.out.OrderRepositoryPort;
import Application.domain.ports.out.ShipmentRepositoryPort;

import java.util.List;

// todo lo que hace un operador logistico sobre un envio: tomarlo,
// despacharlo y entregarlo (coincide 1 a 1 con LogisticsOperatorPort).
// como un pedido puede tener varios envios (uno por vendedor), el
// pedido pasa a SHIPPED en cuanto el primero de sus envios se despacha,
// pero solo pasa a DELIVERED cuando TODOS sus envios ya fueron entregados
public class ShipmentService {

    private final ShipmentRepositoryPort shipmentRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final AuditRepositoryPort auditRepositoryPort;

    public ShipmentService(ShipmentRepositoryPort shipmentRepositoryPort, OrderRepositoryPort orderRepositoryPort,
                            AuditRepositoryPort auditRepositoryPort) {
        this.shipmentRepositoryPort = shipmentRepositoryPort;
        this.orderRepositoryPort = orderRepositoryPort;
        this.auditRepositoryPort = auditRepositoryPort;
    }

    public Shipment assignOperator(Shipment shipment, LogisticsOperator operator) {
        shipment.assignOperator(operator);
        return shipmentRepositoryPort.save(shipment);
    }

    public Shipment markShipped(Shipment shipment) {
        shipment.ship();
        shipment = shipmentRepositoryPort.save(shipment);

        Order order = shipment.getOrder();
        order.ship();
        orderRepositoryPort.save(order);

        auditRepositoryPort.log(new AuditLog(AuditEventType.ORDER_SHIPPED, order.getBuyer().getEmail(), "BUYER",
                "se despacho un envio del pedido " + order.getId()));
        return shipment;
    }

    public Shipment markDelivered(Shipment shipment) {
        shipment.deliver();
        shipment = shipmentRepositoryPort.save(shipment);

        Order order = shipment.getOrder();
        if (allShipmentsDelivered(order.getId())) {
            order.deliver();
            orderRepositoryPort.save(order);
            auditRepositoryPort.log(new AuditLog(AuditEventType.ORDER_DELIVERED, order.getBuyer().getEmail(), "BUYER",
                    "se entrego el pedido " + order.getId()));
        }
        return shipment;
    }

    // mientras algun otro envio del mismo pedido siga sin llegar, el pedido
    // no puede pasar a DELIVERED todavia
    private boolean allShipmentsDelivered(Long orderId) {
        List<Shipment> shipments = shipmentRepositoryPort.findByOrderId(orderId);
        for (Shipment shipment : shipments) {
            if (shipment.getStatus() != ShipmentStatus.DELIVERED) {
                return false;
            }
        }
        return true;
    }
}

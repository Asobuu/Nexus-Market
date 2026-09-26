package Application.service;

import Application.model.Invoice;
import Application.model.Order;
import Application.model.Shipment;
import Application.model.Warehouse;
import Application.repository.InvoiceRepository;
import Application.repository.OrderRepository;
import Application.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

// ciclo de vida del pedido: pagar, generar el envio y entregarlo
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final ShipmentRepository shipmentRepository;

    public OrderService(OrderRepository orderRepository, InvoiceRepository invoiceRepository,
                         ShipmentRepository shipmentRepository) {
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.shipmentRepository = shipmentRepository;
    }

    // marca el pedido como pagado y genera la factura
    public Invoice payOrder(Order order) {
        order.pay();
        orderRepository.save(order);

        Invoice invoice = new Invoice(order);
        return invoiceRepository.save(invoice);
    }

    // crea el envio para un pedido ya pagado y lo pasa a SHIPPED
    public Shipment shipOrder(Order order, Warehouse originWarehouse) {
        Shipment shipment = new Shipment(order, originWarehouse);
        shipment.ship();
        shipmentRepository.save(shipment);

        order.ship();
        orderRepository.save(order);

        return shipment;
    }

    public void deliverOrder(Order order, Shipment shipment) {
        shipment.deliver();
        shipmentRepository.save(shipment);

        order.deliver();
        orderRepository.save(order);
    }
}

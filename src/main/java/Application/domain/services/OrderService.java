package Application.domain.services;

import Application.domain.model.Order;
import Application.domain.model.OrderItem;
import Application.domain.model.Shipment;
import Application.domain.model.Warehouse;
import Application.domain.ports.out.ShipmentRepositoryPort;
import Application.domain.validators.WarehouseOwnershipValidator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// crea los envios de un pedido ya pagado. cada OrderItem ya sabe de que
// bodega se reservo su stock (ver CheckoutService), asi que aca solo
// hay que agrupar los items por bodega: cada grupo se convierte en un
// Shipment distinto. un pedido con productos de varios vendedores
// (normal en un marketplace) termina con un Shipment por cada
// bodega/vendedor involucrado. los productos digitales no generan envio.
// el pago quedo en PaymentService, aparte: son responsabilidades distintas
public class OrderService {

    private final ShipmentRepositoryPort shipmentRepositoryPort;
    private final WarehouseOwnershipValidator warehouseOwnershipValidator;

    public OrderService(ShipmentRepositoryPort shipmentRepositoryPort,
                         WarehouseOwnershipValidator warehouseOwnershipValidator) {
        this.shipmentRepositoryPort = shipmentRepositoryPort;
        this.warehouseOwnershipValidator = warehouseOwnershipValidator;
    }

    public List<Shipment> createShipments(Order order) {
        Map<Long, List<OrderItem>> itemsByWarehouseId = groupShippableItemsByWarehouse(order.getItems());

        List<Shipment> shipments = new ArrayList<>();
        for (List<OrderItem> warehouseItems : itemsByWarehouseId.values()) {
            Warehouse warehouse = warehouseItems.get(0).getWarehouse();
            // red de seguridad: confirma que la bodega donde ya se reservo
            // el stock de verdad sea del vendedor correcto
            warehouseOwnershipValidator.validate(warehouseItems, warehouse);

            Shipment shipment = new Shipment(order, warehouse, warehouseItems);
            shipments.add(shipmentRepositoryPort.save(shipment));
        }
        return shipments;
    }

    private Map<Long, List<OrderItem>> groupShippableItemsByWarehouse(List<OrderItem> items) {
        Map<Long, List<OrderItem>> grouped = new LinkedHashMap<>();
        for (OrderItem item : items) {
            if (!item.getProduct().needsShipping()) {
                continue;
            }
            Long warehouseId = item.getWarehouse().getId();
            grouped.computeIfAbsent(warehouseId, key -> new ArrayList<>()).add(item);
        }
        return grouped;
    }
}

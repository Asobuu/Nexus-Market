package Application.adapters.rest.dto.response;

import Application.domain.enums.ShipmentStatus;
import Application.domain.model.Shipment;
import lombok.Getter;

@Getter
public class ShipmentResponse {

    private final Long id;
    private final Long orderId;
    private final Long originWarehouseId;
    private final Long operatorId;
    private final ShipmentStatus status;
    private final int itemCount;

    private ShipmentResponse(Long id, Long orderId, Long originWarehouseId, Long operatorId, ShipmentStatus status,
                              int itemCount) {
        this.id = id;
        this.orderId = orderId;
        this.originWarehouseId = originWarehouseId;
        this.operatorId = operatorId;
        this.status = status;
        this.itemCount = itemCount;
    }

    public static ShipmentResponse from(Shipment shipment) {
        Long operatorId = shipment.getOperator() == null ? null : shipment.getOperator().getId();
        return new ShipmentResponse(shipment.getId(), shipment.getOrder().getId(), shipment.getOriginWarehouse().getId(),
                operatorId, shipment.getStatus(), shipment.getItems().size());
    }
}

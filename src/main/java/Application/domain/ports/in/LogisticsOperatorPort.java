package Application.domain.ports.in;

import Application.domain.model.Shipment;

// requiere sesion con rol LOGISTICS_OPERATOR
public interface LogisticsOperatorPort {
    Shipment assignSelf(Long operatorId, Long shipmentId);
    Shipment markShipped(Long operatorId, Long shipmentId);
    Shipment markDelivered(Long operatorId, Long shipmentId);
}

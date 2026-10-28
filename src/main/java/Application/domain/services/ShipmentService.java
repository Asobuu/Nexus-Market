package Application.domain.services;

import Application.domain.model.Shipment;
import Application.domain.model.user.LogisticsOperator;
import Application.domain.ports.out.ShipmentRepositoryPort;

// asigna que operador logistico se encarga de un envio
public class ShipmentService {

    private final ShipmentRepositoryPort shipmentRepositoryPort;

    public ShipmentService(ShipmentRepositoryPort shipmentRepositoryPort) {
        this.shipmentRepositoryPort = shipmentRepositoryPort;
    }

    public Shipment assignOperator(Shipment shipment, LogisticsOperator operator) {
        shipment.assignOperator(operator);
        return shipmentRepositoryPort.save(shipment);
    }
}

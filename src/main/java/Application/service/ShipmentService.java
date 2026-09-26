package Application.service;

import Application.model.Shipment;
import Application.model.user.LogisticsOperator;
import Application.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

// asigna que operador logistico se encarga de empacar y despachar un envio
@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public Shipment assignOperator(Shipment shipment, LogisticsOperator operator) {
        shipment.assignOperator(operator);
        return shipmentRepository.save(shipment);
    }
}

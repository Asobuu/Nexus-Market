package Application.adapters.useCases;

import Application.domain.exceptions.EntityNotFoundException;
import Application.domain.exceptions.UnauthorizedActionException;
import Application.domain.model.Shipment;
import Application.domain.model.user.LogisticsOperator;
import Application.domain.ports.in.LogisticsOperatorPort;
import Application.domain.ports.out.LogisticsOperatorRepositoryPort;
import Application.domain.ports.out.ShipmentRepositoryPort;
import Application.domain.services.ShipmentService;
import org.springframework.stereotype.Component;

// todo lo que puede hacer un operador logistico sobre un envio. el SDD
// solo lista ShipmentService, pero resolver operatorId/shipmentId y
// confirmar que el envio es del operador que lo esta despachando o
// entregando necesita tambien LogisticsOperatorRepositoryPort y
// ShipmentRepositoryPort
@Component
public class LogisticsOperatorUseCaseAdapter implements LogisticsOperatorPort {

    private final LogisticsOperatorRepositoryPort logisticsOperatorRepositoryPort;
    private final ShipmentRepositoryPort shipmentRepositoryPort;
    private final ShipmentService shipmentService;

    public LogisticsOperatorUseCaseAdapter(LogisticsOperatorRepositoryPort logisticsOperatorRepositoryPort,
                                            ShipmentRepositoryPort shipmentRepositoryPort,
                                            ShipmentService shipmentService) {
        this.logisticsOperatorRepositoryPort = logisticsOperatorRepositoryPort;
        this.shipmentRepositoryPort = shipmentRepositoryPort;
        this.shipmentService = shipmentService;
    }

    @Override
    public Shipment assignSelf(Long operatorId, Long shipmentId) {
        LogisticsOperator operator = logisticsOperatorRepositoryPort.findById(operatorId)
                .orElseThrow(() -> new UnauthorizedActionException("el operador " + operatorId + " no existe"));
        Shipment shipment = shipmentRepositoryPort.findById(shipmentId)
                .orElseThrow(() -> new EntityNotFoundException("envio", shipmentId));
        return shipmentService.assignOperator(shipment, operator);
    }

    @Override
    public Shipment markShipped(Long operatorId, Long shipmentId) {
        Shipment shipment = requireAssignedShipment(operatorId, shipmentId);
        return shipmentService.markShipped(shipment);
    }

    @Override
    public Shipment markDelivered(Long operatorId, Long shipmentId) {
        Shipment shipment = requireAssignedShipment(operatorId, shipmentId);
        return shipmentService.markDelivered(shipment);
    }

    // solo el operador al que ya se le asigno el envio (ver assignSelf)
    // puede despacharlo o entregarlo; que exista no basta, tiene que ser el suyo
    private Shipment requireAssignedShipment(Long operatorId, Long shipmentId) {
        Shipment shipment = shipmentRepositoryPort.findById(shipmentId)
                .orElseThrow(() -> new EntityNotFoundException("envio", shipmentId));
        if (shipment.getOperator() == null || !shipment.getOperator().getId().equals(operatorId)) {
            throw new UnauthorizedActionException(
                    "el envio " + shipmentId + " no esta asignado al operador " + operatorId);
        }
        return shipment;
    }
}

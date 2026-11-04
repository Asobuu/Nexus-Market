package Application.adapters.rest;

import Application.adapters.rest.dto.response.ShipmentResponse;
import Application.domain.model.Shipment;
import Application.domain.ports.in.LogisticsOperatorPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final LogisticsOperatorPort logisticsOperatorPort;

    public ShipmentController(LogisticsOperatorPort logisticsOperatorPort) {
        this.logisticsOperatorPort = logisticsOperatorPort;
    }

    @PostMapping("/{id}/assign-self")
    public ResponseEntity<ShipmentResponse> assignSelf(@AuthenticationPrincipal Long operatorId,
                                                          @PathVariable Long id) {
        Shipment shipment = logisticsOperatorPort.assignSelf(operatorId, id);
        return ResponseEntity.ok(ShipmentResponse.from(shipment));
    }

    @PostMapping("/{id}/ship")
    public ResponseEntity<ShipmentResponse> ship(@AuthenticationPrincipal Long operatorId, @PathVariable Long id) {
        Shipment shipment = logisticsOperatorPort.markShipped(operatorId, id);
        return ResponseEntity.ok(ShipmentResponse.from(shipment));
    }

    @PostMapping("/{id}/deliver")
    public ResponseEntity<ShipmentResponse> deliver(@AuthenticationPrincipal Long operatorId, @PathVariable Long id) {
        Shipment shipment = logisticsOperatorPort.markDelivered(operatorId, id);
        return ResponseEntity.ok(ShipmentResponse.from(shipment));
    }
}

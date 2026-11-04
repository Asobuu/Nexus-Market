package Application.adapters.rest;

import Application.adapters.rest.dto.request.ApproveReturnRequest;
import Application.adapters.rest.dto.request.RequestReturnRequest;
import Application.adapters.rest.dto.response.ReturnResponse;
import Application.domain.model.Return;
import Application.domain.ports.in.AdministratorPort;
import Application.domain.ports.in.BuyerPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/returns")
public class ReturnController {

    private final BuyerPort buyerPort;
    private final AdministratorPort administratorPort;

    public ReturnController(BuyerPort buyerPort, AdministratorPort administratorPort) {
        this.buyerPort = buyerPort;
        this.administratorPort = administratorPort;
    }

    @PostMapping
    public ResponseEntity<ReturnResponse> request(@AuthenticationPrincipal Long buyerId,
                                                     @Valid @RequestBody RequestReturnRequest request) {
        Return returnRequest = buyerPort.requestReturn(buyerId, request.getOrderId(), request.getProductId(),
                request.getReason());
        return ResponseEntity.status(HttpStatus.CREATED).body(ReturnResponse.from(returnRequest));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ReturnResponse> approve(@AuthenticationPrincipal Long adminId, @PathVariable Long id,
                                                    @Valid @RequestBody ApproveReturnRequest request) {
        Return returnRequest = administratorPort.approveReturn(adminId, id, request.getRefundAmount());
        return ResponseEntity.ok(ReturnResponse.from(returnRequest));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ReturnResponse> reject(@AuthenticationPrincipal Long adminId, @PathVariable Long id) {
        Return returnRequest = administratorPort.rejectReturn(adminId, id);
        return ResponseEntity.ok(ReturnResponse.from(returnRequest));
    }

    // no esta en la tabla de Api-rest-endpoints.md (solo lista approve y
    // reject), pero sin ella AdministratorPort.completeRefund nunca se
    // podria llamar - y es justo el paso que repone el stock al vendedor
    // correcto (ver ReturnService.completeRefund), que es lo que el
    // profesor pidio revisar explicitamente
    @PostMapping("/{id}/complete-refund")
    public ResponseEntity<ReturnResponse> completeRefund(@AuthenticationPrincipal Long adminId,
                                                            @PathVariable Long id) {
        Return returnRequest = administratorPort.completeRefund(adminId, id);
        return ResponseEntity.ok(ReturnResponse.from(returnRequest));
    }
}

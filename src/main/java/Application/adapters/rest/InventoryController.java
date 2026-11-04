package Application.adapters.rest;

import Application.adapters.rest.dto.request.AddStockRequest;
import Application.adapters.rest.dto.response.InventoryResponse;
import Application.domain.model.Inventory;
import Application.domain.ports.in.SellerPort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final SellerPort sellerPort;

    public InventoryController(SellerPort sellerPort) {
        this.sellerPort = sellerPort;
    }

    @PostMapping("/{id}/receipt")
    public ResponseEntity<InventoryResponse> addStock(@AuthenticationPrincipal Long sellerId, @PathVariable Long id,
                                                        @Valid @RequestBody AddStockRequest request) {
        Inventory inventory = sellerPort.addStock(sellerId, id, request.getAmount());
        return ResponseEntity.ok(InventoryResponse.from(inventory));
    }
}

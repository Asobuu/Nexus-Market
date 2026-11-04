package Application.adapters.rest;

import Application.adapters.rest.dto.response.CountResponse;
import Application.adapters.rest.dto.response.InventoryResponse;
import Application.adapters.rest.dto.response.TotalSalesResponse;
import Application.domain.enums.ProductStatus;
import Application.domain.ports.in.SupervisorPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// SupervisorPort no recibe ningun id de actor (ver
// SupervisorUseCaseAdapter), asi que ningun metodo de aca necesita
// @AuthenticationPrincipal; el control de acceso es solo por rol
// (ver SecurityConfig: GET /api/reports/** -> hasRole("SUPERVISOR"))
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final SupervisorPort supervisorPort;

    public ReportController(SupervisorPort supervisorPort) {
        this.supervisorPort = supervisorPort;
    }

    @GetMapping("/sales")
    public ResponseEntity<TotalSalesResponse> totalSales() {
        return ResponseEntity.ok(new TotalSalesResponse(supervisorPort.getTotalSales()));
    }

    @GetMapping("/orders/pending")
    public ResponseEntity<CountResponse> pendingOrders() {
        return ResponseEntity.ok(new CountResponse(supervisorPort.countPendingOrders()));
    }

    @GetMapping("/products/status")
    public ResponseEntity<CountResponse> productsByStatus(@RequestParam ProductStatus status) {
        return ResponseEntity.ok(new CountResponse(supervisorPort.countProductsByStatus(status)));
    }

    @GetMapping("/inventory/low-stock")
    public ResponseEntity<List<InventoryResponse>> lowStock(@RequestParam int threshold) {
        List<InventoryResponse> inventories = supervisorPort.getLowStock(threshold).stream()
                .map(InventoryResponse::from).toList();
        return ResponseEntity.ok(inventories);
    }
}

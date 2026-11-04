package Application.adapters.rest;

import Application.adapters.rest.dto.request.RegisterOperatorRequest;
import Application.adapters.rest.dto.request.RegisterSellerRequest;
import Application.adapters.rest.dto.request.RegisterWarehouseRequest;
import Application.adapters.rest.dto.response.LogisticsOperatorResponse;
import Application.adapters.rest.dto.response.SellerResponse;
import Application.adapters.rest.dto.response.WarehouseResponse;
import Application.domain.model.Warehouse;
import Application.domain.model.user.LogisticsOperator;
import Application.domain.model.user.Seller;
import Application.domain.ports.in.AdministratorPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdministratorPort administratorPort;

    public AdminController(AdministratorPort administratorPort) {
        this.administratorPort = administratorPort;
    }

    @PostMapping("/sellers")
    public ResponseEntity<SellerResponse> registerSeller(@AuthenticationPrincipal Long adminId,
                                                            @Valid @RequestBody RegisterSellerRequest request) {
        Seller seller = administratorPort.registerSeller(adminId, request.getFullName(), request.getEmail(),
                request.getRawPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(SellerResponse.from(seller));
    }

    @PostMapping("/logistics-operators")
    public ResponseEntity<LogisticsOperatorResponse> registerOperator(@AuthenticationPrincipal Long adminId,
                                                                        @Valid @RequestBody RegisterOperatorRequest request) {
        LogisticsOperator operator = administratorPort.registerLogisticsOperator(adminId, request.getFullName(),
                request.getEmail(), request.getRawPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(LogisticsOperatorResponse.from(operator));
    }

    @PostMapping("/warehouses")
    public ResponseEntity<WarehouseResponse> registerWarehouse(@AuthenticationPrincipal Long adminId,
                                                                  @Valid @RequestBody RegisterWarehouseRequest request) {
        Warehouse warehouse = administratorPort.registerWarehouse(adminId, request.getName(),
                request.getLocation().toDomain(), request.getOwnerSellerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(WarehouseResponse.from(warehouse));
    }
}

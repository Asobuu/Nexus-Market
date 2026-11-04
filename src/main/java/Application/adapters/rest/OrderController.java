package Application.adapters.rest;

import Application.adapters.rest.dto.response.InvoiceResponse;
import Application.adapters.rest.dto.response.OrderResponse;
import Application.domain.model.Invoice;
import Application.domain.ports.in.BuyerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final BuyerPort buyerPort;

    public OrderController(BuyerPort buyerPort) {
        this.buyerPort = buyerPort;
    }

    @GetMapping("/mine")
    public ResponseEntity<List<OrderResponse>> viewMine(@AuthenticationPrincipal Long buyerId) {
        List<OrderResponse> orders = buyerPort.viewMyOrders(buyerId).stream().map(OrderResponse::from).toList();
        return ResponseEntity.ok(orders);
    }

    // no esta en la tabla de Api-rest-endpoints.md (quedo incompleta: no
    // lista ninguna ruta para BuyerPort.payOrder), pero sin ella nunca se
    // podria pagar un pedido ni disparar la creacion de sus envios (ver
    // BuyerUseCaseAdapter.payOrder)
    @PostMapping("/{id}/pay")
    public ResponseEntity<InvoiceResponse> pay(@AuthenticationPrincipal Long buyerId, @PathVariable Long id) {
        Invoice invoice = buyerPort.payOrder(buyerId, id);
        return ResponseEntity.ok(InvoiceResponse.from(invoice));
    }
}

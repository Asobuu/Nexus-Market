package Application.adapters.rest;

import Application.adapters.rest.dto.request.AddToCartRequest;
import Application.adapters.rest.dto.request.CheckoutRequest;
import Application.adapters.rest.dto.response.CartResponse;
import Application.adapters.rest.dto.response.OrderResponse;
import Application.domain.model.Cart;
import Application.domain.model.Order;
import Application.domain.ports.in.BuyerPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final BuyerPort buyerPort;

    public CartController(BuyerPort buyerPort) {
        this.buyerPort = buyerPort;
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@AuthenticationPrincipal Long buyerId,
                                                  @Valid @RequestBody AddToCartRequest request) {
        Cart cart = buyerPort.addToCart(buyerId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(CartResponse.from(cart));
    }

    @GetMapping
    public ResponseEntity<CartResponse> view(@AuthenticationPrincipal Long buyerId) {
        Cart cart = buyerPort.viewCart(buyerId);
        return ResponseEntity.ok(CartResponse.from(cart));
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@AuthenticationPrincipal Long buyerId,
                                                    @Valid @RequestBody CheckoutRequest request) {
        Order order = buyerPort.checkout(buyerId, request.getDeliveryAddress().toDomain());
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(order));
    }
}

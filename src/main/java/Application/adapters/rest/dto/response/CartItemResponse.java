package Application.adapters.rest.dto.response;

import Application.domain.model.CartItem;
import lombok.Getter;

@Getter
public class CartItemResponse {

    private final Long id;
    private final Long productId;
    private final String productName;
    private final int quantity;
    private final double subtotal;

    private CartItemResponse(Long id, Long productId, String productName, int quantity, double subtotal) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public static CartItemResponse from(CartItem item) {
        return new CartItemResponse(item.getId(), item.getProduct().getId(), item.getProduct().getName(),
                item.getQuantity(), item.getSubtotal());
    }
}

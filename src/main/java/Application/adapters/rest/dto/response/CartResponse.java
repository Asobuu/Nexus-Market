package Application.adapters.rest.dto.response;

import Application.domain.model.Cart;
import lombok.Getter;

import java.util.List;

@Getter
public class CartResponse {

    private final Long id;
    private final Long buyerId;
    private final List<CartItemResponse> items;
    private final double total;

    private CartResponse(Long id, Long buyerId, List<CartItemResponse> items, double total) {
        this.id = id;
        this.buyerId = buyerId;
        this.items = items;
        this.total = total;
    }

    public static CartResponse from(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream().map(CartItemResponse::from).toList();
        // un carrito recien creado (nunca guardado) no tiene id ni buyerId
        // propio todavia, pero si sabemos quien es el dueño (ver
        // BuyerUseCaseAdapter.viewCart)
        Long buyerId = cart.getBuyer() == null ? null : cart.getBuyer().getId();
        return new CartResponse(cart.getId(), buyerId, items, cart.getTotal());
    }
}

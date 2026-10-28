package Application.domain.services;

import Application.domain.model.Cart;
import Application.domain.model.CartItem;
import Application.domain.model.Product;
import Application.domain.ports.out.CartRepositoryPort;

// solo administra el carrito: agregar productos, verlo, vaciarlo.
// no sabe nada de pedidos ni de inventario, eso es tarea de CheckoutService
public class CartService {

    private final CartRepositoryPort cartRepositoryPort;

    public CartService(CartRepositoryPort cartRepositoryPort) {
        this.cartRepositoryPort = cartRepositoryPort;
    }

    public Cart addToCart(Cart cart, Product product, int quantity) {
        cart.addItem(new CartItem(product, quantity));
        return cartRepositoryPort.save(cart);
    }

    public Cart clear(Cart cart) {
        cart.clear();
        return cartRepositoryPort.save(cart);
    }
}

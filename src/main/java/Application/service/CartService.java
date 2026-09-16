package Application.service;

import Application.model.Cart;
import Application.model.CartItem;
import Application.model.Inventory;
import Application.model.Order;
import Application.model.OrderItem;
import Application.model.Product;
import Application.model.user.Address;
import Application.repository.CartRepository;
import Application.repository.InventoryRepository;
import Application.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// arma el carrito y lo convierte en pedido cuando el comprador decide pagar
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;

    public CartService(CartRepository cartRepository, InventoryRepository inventoryRepository,
                        OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
    }

    public Cart addToCart(Cart cart, Product product, int quantity) {
        cart.addItem(new CartItem(product, quantity));
        return cartRepository.save(cart);
    }

    // convierte el carrito en un pedido: reserva el stock de cada producto
    // y deja el carrito vacio. "inventories" debe traer un registro por
    // cada item del carrito, en el mismo orden
    public Order checkout(Cart cart, List<Inventory> inventories, Address deliveryAddress) {
        List<CartItem> items = cart.getItems();
        if (items.isEmpty()) {
            throw new IllegalStateException("el carrito esta vacio");
        }
        if (items.size() != inventories.size()) {
            throw new IllegalArgumentException("debe haber un inventario por cada producto del carrito");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            Inventory inventory = inventories.get(i);

            inventory.reserve(item.getQuantity());
            inventoryRepository.save(inventory);

            orderItems.add(new OrderItem(item.getProduct(), item.getQuantity()));
        }

        Order order = new Order(cart.getBuyer(), orderItems, deliveryAddress);
        order = orderRepository.save(order);

        items.clear();
        cartRepository.save(cart);

        return order;
    }
}

package Application.domain.services;

import Application.domain.model.Cart;
import Application.domain.model.CartItem;
import Application.domain.model.Inventory;
import Application.domain.model.Order;
import Application.domain.model.OrderItem;
import Application.domain.model.user.Address;
import Application.domain.ports.out.OrderRepositoryPort;

import java.util.ArrayList;
import java.util.List;

// convierte un carrito en un pedido. antes esto estaba mezclado dentro
// de CartService (agregar al carrito y pagar eran la misma clase); lo
// separe porque son responsabilidades distintas: una arma el carrito,
// esta otra lo convierte en una compra confirmada
public class CheckoutService {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryService inventoryService;
    private final CartService cartService;

    public CheckoutService(OrderRepositoryPort orderRepositoryPort, InventoryService inventoryService,
                            CartService cartService) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.inventoryService = inventoryService;
        this.cartService = cartService;
    }

    // "inventories" debe traer un registro por cada item del carrito,
    // en el mismo orden
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

            inventoryService.reserveStock(inventory, item.getQuantity());

            orderItems.add(new OrderItem(item.getProduct(), item.getQuantity()));
        }

        Order order = new Order(cart.getBuyer(), orderItems, deliveryAddress);
        order = orderRepositoryPort.save(order);

        cartService.clear(cart);

        return order;
    }
}

package Application.domain.services;

import Application.domain.model.Cart;
import Application.domain.model.CartItem;
import Application.domain.model.Inventory;
import Application.domain.model.Order;
import Application.domain.model.OrderItem;
import Application.domain.model.Product;
import Application.domain.model.Warehouse;
import Application.domain.model.user.Address;
import Application.domain.ports.out.OrderRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// convierte un carrito en un pedido, reservando el stock de cada
// producto fisico del carrito. antes esto estaba mezclado dentro de
// CartService (agregar al carrito y pagar eran la misma clase); lo
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

    // "inventoryByProductId" dice de que inventario (bodega + stock) sale
    // cada producto FISICO del carrito. los productos digitales no
    // necesitan entrada ahi, porque no tienen inventario (ver
    // Product.needsShipping). el inventario elegido aca queda grabado en
    // cada OrderItem y es lo que despues usa OrderService para armar los
    // envios: el producto sale exactamente de donde se le reservo el stock
    public Order checkout(Cart cart, Map<Long, Inventory> inventoryByProductId, Address deliveryAddress) {
        List<CartItem> items = cart.getItems();
        if (items.isEmpty()) {
            throw new IllegalStateException("el carrito esta vacio");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : items) {
            Product product = item.getProduct();
            Warehouse warehouse = null;

            if (product.needsShipping()) {
                Inventory inventory = inventoryByProductId.get(product.getId());
                if (inventory == null) {
                    throw new IllegalArgumentException(
                            "falta el inventario de origen para el producto " + product.getName());
                }
                inventoryService.reserveStock(inventory, item.getQuantity());
                warehouse = inventory.getWarehouse();
            }

            orderItems.add(new OrderItem(product, item.getQuantity(), warehouse));
        }

        Order order = new Order(cart.getBuyer(), orderItems, deliveryAddress);
        order = orderRepositoryPort.save(order);

        cartService.clear(cart);

        return order;
    }
}

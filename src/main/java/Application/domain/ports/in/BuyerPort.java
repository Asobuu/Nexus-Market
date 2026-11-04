package Application.domain.ports.in;

import Application.domain.model.Cart;
import Application.domain.model.Invoice;
import Application.domain.model.Order;
import Application.domain.model.Return;
import Application.domain.model.user.Address;

import java.util.List;

// requiere sesion con rol BUYER
public interface BuyerPort {
    Cart addToCart(Long buyerId, Long productId, int quantity);
    Cart viewCart(Long buyerId);
    Order checkout(Long buyerId, Address deliveryAddress);
    // pago simulado: el comprador confirma y el pedido queda pagado
    // (no hay pasarela de pago real todavia)
    Invoice payOrder(Long buyerId, Long orderId);
    List<Order> viewMyOrders(Long buyerId);
    Return requestReturn(Long buyerId, Long orderId, Long productId, String reason);
}

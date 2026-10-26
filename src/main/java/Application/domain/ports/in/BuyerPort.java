package Application.domain.ports.in;

import Application.domain.model.Cart;
import Application.domain.model.Order;
import Application.domain.model.Return;
import Application.domain.model.user.Address;

import java.util.List;

// requiere sesion con rol BUYER
public interface BuyerPort {
    Cart addToCart(Long buyerId, Long productId, int quantity);
    Cart viewCart(Long buyerId);
    Order checkout(Long buyerId, Address deliveryAddress);
    List<Order> viewMyOrders(Long buyerId);
    Return requestReturn(Long buyerId, Long orderId, Long productId, String reason);
}

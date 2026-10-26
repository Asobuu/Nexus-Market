package Application.domain.model;

import Application.domain.enums.OrderStatus;
import Application.domain.exceptions.InvalidOrderStateException;
import Application.domain.model.user.Address;
import Application.domain.model.user.Buyer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// una compra ya confirmada. va pasando por varios estados y una vez
// llega a DELIVERED no se deja tocar mas
@Getter
@NoArgsConstructor
public class Order {

    private Long id;
    private Buyer buyer;
    private List<OrderItem> items = new ArrayList<>();
    private OrderStatus status;
    private Address deliveryAddress;

    public Order(Buyer buyer, List<OrderItem> items, Address deliveryAddress) {
        this.buyer = buyer;
        // copio la lista en vez de guardar la que me pasan, porque a veces
        // llega una lista de solo lectura y despues no se podria tocar
        this.items = new ArrayList<>(items);
        this.deliveryAddress = deliveryAddress;
        this.status = OrderStatus.PENDING_PAYMENT;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void pay() {
        checkNotDelivered();
        status = OrderStatus.PAID;
    }

    public void ship() {
        checkNotDelivered();
        status = OrderStatus.SHIPPED;
    }

    public void deliver() {
        checkNotDelivered();
        status = OrderStatus.DELIVERED;
    }

    private void checkNotDelivered() {
        if (status == OrderStatus.DELIVERED) {
            throw new InvalidOrderStateException("el pedido " + id + " ya fue entregado, no se puede modificar");
        }
    }

    public double getTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
}

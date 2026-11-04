package Application.domain.model;

import Application.domain.enums.OrderStatus;
import Application.domain.exceptions.InvalidOrderStateException;
import Application.domain.model.user.Address;
import Application.domain.model.user.Buyer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// una compra ya confirmada. siempre avanza en el mismo orden
// (PENDING_PAYMENT -> PAID -> SHIPPED -> DELIVERED) y una vez llega a
// DELIVERED no se deja tocar mas. cada salto solo es valido desde el
// estado inmediatamente anterior, para que no se pueda, por ejemplo,
// pagar dos veces o entregar algo que nunca se despacho.
// "ship()" es la excepcion: se puede llamar mas de una vez sin error,
// porque un pedido con productos de varios vendedores tiene varios
// envios (ver ShipmentService), y cada uno pasa por aca al despacharse
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
        requireStatus(OrderStatus.PENDING_PAYMENT);
        status = OrderStatus.PAID;
    }

    public void ship() {
        if (status == OrderStatus.SHIPPED) {
            return;
        }
        requireStatus(OrderStatus.PAID);
        status = OrderStatus.SHIPPED;
    }

    public void deliver() {
        requireStatus(OrderStatus.SHIPPED);
        status = OrderStatus.DELIVERED;
    }

    // la usa el mapper de persistencia para reconstruir un pedido ya
    // guardado, saltandose las validaciones de transicion: esas son para
    // cuando alguien pide pagar/despachar/entregar, no para cargar un
    // estado que ya quedo grabado tal cual
    public void assignStatus(OrderStatus status) {
        this.status = status;
    }

    private void requireStatus(OrderStatus expected) {
        if (status != expected) {
            throw new InvalidOrderStateException(
                    "el pedido " + id + " esta en estado " + status + ", se esperaba " + expected);
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

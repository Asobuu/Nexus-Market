package Application.model;

import Application.enums.OrderStatus;
import Application.model.user.Address;
import Application.model.user.Buyer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// una compra ya confirmada. va pasando por varios estados y una vez
// llega a DELIVERED no se deja tocar mas
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Embedded
    private Address deliveryAddress;

    public Order(Buyer buyer, List<OrderItem> items, Address deliveryAddress) {
        this.buyer = buyer;
        // copio la lista en vez de guardar la que me pasan, porque a veces
        // llega una lista de solo lectura (List.of(...)) y despues hibernate
        // se queja si intenta tocarla
        this.items = new ArrayList<>(items);
        this.deliveryAddress = deliveryAddress;
        this.status = OrderStatus.PENDING_PAYMENT;
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
            throw new IllegalStateException("el pedido " + id + " ya fue entregado, no se puede modificar");
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

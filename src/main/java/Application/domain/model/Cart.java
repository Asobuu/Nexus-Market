package Application.domain.model;

import Application.domain.model.user.Buyer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// lo que el comprador va guardando antes de decidirse a pagar.
// esta clase solo administra el carrito (agregar, ver, vaciar); convertirlo
// en un pedido es responsabilidad de CheckoutService, no de aqui
@Getter
@NoArgsConstructor
public class Cart {

    private Long id;
    private Buyer buyer;
    private List<CartItem> items = new ArrayList<>();

    public Cart(Buyer buyer) {
        this.buyer = buyer;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void addItem(CartItem item) {
        items.add(item);
    }

    public void clear() {
        items.clear();
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
}

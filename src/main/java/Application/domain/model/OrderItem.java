package Application.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

// una linea ya confirmada de un pedido. el precio se copia del producto
// en el momento de la compra, para que si despues cambia el precio no
// afecte pedidos que ya estaban hechos
@Getter
@NoArgsConstructor
public class OrderItem {

    private Long id;
    private Product product;
    private int quantity;
    private double unitPrice;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public double getSubtotal() {
        return unitPrice * quantity;
    }
}

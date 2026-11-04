package Application.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

// una linea ya confirmada de un pedido. el precio se copia del producto
// en el momento de la compra, para que si despues cambia el precio no
// afecte pedidos que ya estaban hechos.
// "warehouse" es de donde se reservo el stock de este item en el
// checkout (ver CheckoutService): es la fuente de verdad de donde sale
// fisicamente el producto, y es lo que despues usa OrderService para
// armar los envios. queda null si el producto es digital y no necesita
// envio (ver Product.needsShipping)
@Getter
@NoArgsConstructor
public class OrderItem {

    private Long id;
    private Product product;
    private int quantity;
    private double unitPrice;
    private Warehouse warehouse;

    public OrderItem(Product product, int quantity, Warehouse warehouse) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
        this.warehouse = warehouse;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    // la usa el mapper de persistencia: el constructor copia el precio
    // ACTUAL del producto, pero al reconstruir un item ya guardado hay que
    // restaurar el precio con el que de verdad se compro, no el de hoy
    public void assignUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getSubtotal() {
        return unitPrice * quantity;
    }
}

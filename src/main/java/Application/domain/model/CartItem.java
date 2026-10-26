package Application.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

// una linea dentro del carrito: un producto y cuantos quiere el comprador
@Getter
@NoArgsConstructor
public class CartItem {

    private Long id;
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }
}

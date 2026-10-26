package Application.domain.model;

import Application.domain.exceptions.InsufficientStockException;
import lombok.Getter;
import lombok.NoArgsConstructor;

// cuanto stock hay de un producto en una bodega especifica.
// el mismo producto puede tener stock en varias bodegas (cada una con
// su propio registro de Inventory)
@Getter
@NoArgsConstructor
public class Inventory {

    private Long id;
    private Product product;
    private Warehouse warehouse;
    private int quantity;

    public Inventory(Product product, Warehouse warehouse, int quantity) {
        this.product = product;
        this.warehouse = warehouse;
        this.quantity = quantity;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    // el stock nunca puede quedar negativo
    public void reserve(int amount) {
        if (amount > quantity) {
            throw new InsufficientStockException("no hay suficiente stock de " + product.getName());
        }
        quantity -= amount;
    }

    public void addStock(int amount) {
        quantity += amount;
    }
}

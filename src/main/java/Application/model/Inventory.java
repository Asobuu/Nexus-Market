package Application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// cuanto stock hay de un producto en una bodega especifica.
// el mismo producto puede tener stock en varias bodegas (cada una con
// su propio registro de Inventory)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    private int quantity;

    public Inventory(Product product, Warehouse warehouse, int quantity) {
        this.product = product;
        this.warehouse = warehouse;
        this.quantity = quantity;
    }

    // el stock nunca puede quedar negativo
    public void reserve(int amount) {
        if (amount > quantity) {
            throw new IllegalStateException("no hay suficiente stock de " + product.getName());
        }
        quantity -= amount;
    }

    public void addStock(int amount) {
        quantity += amount;
    }
}

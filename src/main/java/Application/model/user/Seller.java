package Application.model.user;

import Application.model.Product;
import Application.model.Warehouse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// un vendedor administra sus propios productos y bodegas.
// no se registra solo, lo crea un administrador
@Entity
@DiscriminatorValue("SELLER")
@Getter
@Setter
@NoArgsConstructor
public class Seller extends User {

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Warehouse> warehouses = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    public Seller(String fullName, String email) {
        super(fullName, email);
    }

    @Override
    public String getRoleName() {
        return "Seller";
    }

    public void addWarehouse(Warehouse warehouse) {
        warehouses.add(warehouse);
        warehouse.setOwner(this);
    }

    public void addProduct(Product product) {
        products.add(product);
        product.setSeller(this);
    }
}

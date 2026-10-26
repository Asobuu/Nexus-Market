package Application.domain.model.user;

import Application.domain.model.Product;
import Application.domain.model.Warehouse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// un vendedor administra sus propios productos y bodegas.
// no se registra solo, lo crea un administrador
@Getter
@NoArgsConstructor
public class Seller extends User {

    private List<Warehouse> warehouses = new ArrayList<>();
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
        warehouse.assignOwner(this);
    }

    public void addProduct(Product product) {
        products.add(product);
        product.assignSeller(this);
    }
}

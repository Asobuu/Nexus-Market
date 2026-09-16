package Application.service;

import Application.enums.ProductType;
import Application.model.Inventory;
import Application.model.Product;
import Application.model.Warehouse;
import Application.model.user.Seller;
import Application.repository.InventoryRepository;
import Application.repository.ProductRepository;
import org.springframework.stereotype.Service;

// publicar productos y manejar su stock inicial en una bodega
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public ProductService(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Product publishProduct(Seller seller, String name, String description, ProductType type,
                                   double price, Warehouse warehouse, int initialStock) {
        Product product = new Product(name, description, type, price, seller);
        product = productRepository.save(product);

        // los productos digitales no manejan stock en bodega
        if (type == ProductType.PHYSICAL) {
            Inventory inventory = new Inventory(product, warehouse, initialStock);
            inventoryRepository.save(inventory);
        }

        return product;
    }

    public Inventory addStock(Inventory inventory, int amount) {
        inventory.addStock(amount);
        return inventoryRepository.save(inventory);
    }
}

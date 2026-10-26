package Application.service;

import Application.enums.ProductType;
import Application.model.Inventory;
import Application.model.Product;
import Application.model.ProductVariant;
import Application.model.Warehouse;
import Application.model.user.Seller;
import Application.repository.InventoryRepository;
import Application.repository.ProductRepository;
import Application.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;

// publicar productos, crearles su inventario inicial y sus variantes
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;
    private final ProductVariantRepository variantRepository;

    public ProductService(ProductRepository productRepository, InventoryRepository inventoryRepository,
                           InventoryService inventoryService, ProductVariantRepository variantRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryService = inventoryService;
        this.variantRepository = variantRepository;
    }

    public Product publishProduct(Seller seller, String name, String description, ProductType type,
                                   double price, Warehouse warehouse, int initialStock) {
        Product product = new Product(name, description, type, price, seller);
        product = productRepository.save(product);

        // los productos digitales no manejan stock en bodega
        if (type == ProductType.PHYSICAL) {
            Inventory inventory = new Inventory(product, warehouse, 0);
            inventory = inventoryRepository.save(inventory);
            // el stock inicial tambien queda como un movimiento de ingreso
            inventoryService.registerReceipt(inventory, initialStock);
        }

        return product;
    }

    public ProductVariant addVariant(Product product, String color, String size, String model) {
        ProductVariant variant = new ProductVariant(product, color, size, model);
        variant = variantRepository.save(variant);
        product.addVariant(variant);
        return variant;
    }

    public Product suspendProduct(Product product) {
        product.suspend();
        return productRepository.save(product);
    }

    public Product discontinueProduct(Product product) {
        product.discontinue();
        return productRepository.save(product);
    }
}

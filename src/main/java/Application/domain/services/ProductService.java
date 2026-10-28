package Application.domain.services;

import Application.domain.enums.ProductType;
import Application.domain.model.Inventory;
import Application.domain.model.Product;
import Application.domain.model.ProductVariant;
import Application.domain.model.Warehouse;
import Application.domain.model.user.Seller;
import Application.domain.ports.out.InventoryRepositoryPort;
import Application.domain.ports.out.ProductRepositoryPort;
import Application.domain.ports.out.ProductVariantRepositoryPort;

// publicar productos, crearles su inventario inicial y sus variantes
public class ProductService {

    private final ProductRepositoryPort productRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryService inventoryService;
    private final ProductVariantRepositoryPort variantRepositoryPort;

    public ProductService(ProductRepositoryPort productRepositoryPort, InventoryRepositoryPort inventoryRepositoryPort,
                           InventoryService inventoryService, ProductVariantRepositoryPort variantRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.inventoryService = inventoryService;
        this.variantRepositoryPort = variantRepositoryPort;
    }

    public Product publishProduct(Seller seller, String name, String description, ProductType type,
                                   double price, Warehouse warehouse, int initialStock) {
        Product product = new Product(name, description, type, price, seller);
        product = productRepositoryPort.save(product);

        if (type == ProductType.PHYSICAL) {
            Inventory inventory = new Inventory(product, warehouse, 0);
            inventory = inventoryRepositoryPort.save(inventory);
            inventoryService.registerReceipt(inventory, initialStock);
        }

        return product;
    }

    public ProductVariant addVariant(Product product, String color, String size, String model) {
        ProductVariant variant = new ProductVariant(product, color, size, model);
        variant = variantRepositoryPort.save(variant);
        product.addVariant(variant);
        return variant;
    }

    public Product suspendProduct(Product product) {
        product.suspend();
        return productRepositoryPort.save(product);
    }

    public Product discontinueProduct(Product product) {
        product.discontinue();
        return productRepositoryPort.save(product);
    }
}

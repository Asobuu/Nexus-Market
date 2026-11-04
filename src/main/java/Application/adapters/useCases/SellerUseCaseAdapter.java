package Application.adapters.useCases;

import Application.domain.enums.ProductType;
import Application.domain.exceptions.EntityNotFoundException;
import Application.domain.exceptions.UnauthorizedActionException;
import Application.domain.model.Inventory;
import Application.domain.model.Product;
import Application.domain.model.ProductVariant;
import Application.domain.model.Warehouse;
import Application.domain.model.user.Seller;
import Application.domain.ports.in.SellerPort;
import Application.domain.ports.out.InventoryRepositoryPort;
import Application.domain.ports.out.ProductRepositoryPort;
import Application.domain.ports.out.SellerRepositoryPort;
import Application.domain.ports.out.WarehouseRepositoryPort;
import Application.domain.services.InventoryService;
import Application.domain.services.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;

// todo lo que puede hacer un vendedor autenticado sobre su propio
// catalogo e inventario. el SDD solo lista ProductService e
// InventoryService como dependencias, pero resolver los ids del puerto
// (sellerId/productId/warehouseId/inventoryId) y confirmar que lo que
// se esta tocando es realmente del vendedor que hace la peticion
// necesita tambien ProductRepositoryPort, WarehouseRepositoryPort,
// InventoryRepositoryPort y SellerRepositoryPort
@Component
public class SellerUseCaseAdapter implements SellerPort {

    private final SellerRepositoryPort sellerRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final ProductService productService;
    private final InventoryService inventoryService;

    public SellerUseCaseAdapter(SellerRepositoryPort sellerRepositoryPort, ProductRepositoryPort productRepositoryPort,
                                 WarehouseRepositoryPort warehouseRepositoryPort,
                                 InventoryRepositoryPort inventoryRepositoryPort, ProductService productService,
                                 InventoryService inventoryService) {
        this.sellerRepositoryPort = sellerRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
        this.warehouseRepositoryPort = warehouseRepositoryPort;
        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.productService = productService;
        this.inventoryService = inventoryService;
    }

    @Override
    public Product publishProduct(Long sellerId, String name, String description, ProductType type, double price,
                                   Long warehouseId, int initialStock) {
        Seller seller = sellerRepositoryPort.findById(sellerId)
                .orElseThrow(() -> new UnauthorizedActionException("el vendedor " + sellerId + " no existe"));

        // un producto digital no necesita bodega (ver Product.needsShipping);
        // uno fisico si, y tiene que ser una bodega del propio vendedor o del
        // marketplace (owner null = abierta a todos, ver WarehouseOwnershipValidator)
        Warehouse warehouse = null;
        if (warehouseId != null) {
            warehouse = warehouseRepositoryPort.findById(warehouseId)
                    .orElseThrow(() -> new EntityNotFoundException("bodega", warehouseId));
            if (warehouse.getOwner() != null && !warehouse.getOwner().getId().equals(sellerId)) {
                throw new UnauthorizedActionException(
                        "la bodega " + warehouseId + " no pertenece al vendedor " + sellerId);
            }
        }

        return productService.publishProduct(seller, name, description, type, price, warehouse, initialStock);
    }

    @Override
    public ProductVariant addVariant(Long sellerId, Long productId, String color, String size, String model) {
        Product product = requireOwnedProduct(sellerId, productId);
        return productService.addVariant(product, color, size, model);
    }

    @Override
    public Inventory addStock(Long sellerId, Long inventoryId, int amount) {
        Inventory inventory = inventoryRepositoryPort.findById(inventoryId)
                .orElseThrow(() -> new EntityNotFoundException("inventario", inventoryId));
        if (!inventory.getProduct().getSeller().getId().equals(sellerId)) {
            throw new UnauthorizedActionException(
                    "el inventario " + inventoryId + " no pertenece al vendedor " + sellerId);
        }
        return inventoryService.registerReceipt(inventory, amount);
    }

    @Override
    public Product suspendProduct(Long sellerId, Long productId) {
        Product product = requireOwnedProduct(sellerId, productId);
        return productService.suspendProduct(product);
    }

    @Override
    public Product discontinueProduct(Long sellerId, Long productId) {
        Product product = requireOwnedProduct(sellerId, productId);
        return productService.discontinueProduct(product);
    }

    @Override
    public List<Product> viewMyProducts(Long sellerId) {
        return productRepositoryPort.findBySellerId(sellerId);
    }

    private Product requireOwnedProduct(Long sellerId, Long productId) {
        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("producto", productId));
        if (!product.getSeller().getId().equals(sellerId)) {
            throw new UnauthorizedActionException("el producto " + productId + " no pertenece al vendedor " + sellerId);
        }
        return product;
    }
}

package Application.domain.ports.in;

import Application.domain.enums.ProductType;
import Application.domain.model.Inventory;
import Application.domain.model.Product;
import Application.domain.model.ProductVariant;

import java.util.List;

// requiere sesion con rol SELLER
public interface SellerPort {
    Product publishProduct(Long sellerId, String name, String description, ProductType type,
                            double price, Long warehouseId, int initialStock);
    ProductVariant addVariant(Long sellerId, Long productId, String color, String size, String model);
    Inventory addStock(Long sellerId, Long inventoryId, int amount);
    Product suspendProduct(Long sellerId, Long productId);
    Product discontinueProduct(Long sellerId, Long productId);
    List<Product> viewMyProducts(Long sellerId);
}

package Application.service;

import Application.enums.OrderStatus;
import Application.enums.ProductStatus;
import Application.model.Inventory;
import Application.model.Invoice;
import Application.model.Order;
import Application.model.Product;
import Application.repository.InventoryRepository;
import Application.repository.InvoiceRepository;
import Application.repository.OrderRepository;
import Application.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// consultas consolidadas para el administrador y el supervisor.
// aca no se modifica nada, solo se lee y se cuenta/suma
@Service
public class ReportService {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public ReportService(InvoiceRepository invoiceRepository, OrderRepository orderRepository,
                          ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.invoiceRepository = invoiceRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public double getTotalSales() {
        double total = 0;
        for (Invoice invoice : invoiceRepository.findAll()) {
            total += invoice.getTotalAmount();
        }
        return total;
    }

    // cuenta los pedidos que todavia no llegan a DELIVERED
    public long countPendingOrders() {
        long count = 0;
        for (Order order : orderRepository.findAll()) {
            if (order.getStatus() != OrderStatus.DELIVERED) {
                count++;
            }
        }
        return count;
    }

    public long countProductsByStatus(ProductStatus status) {
        long count = 0;
        for (Product product : productRepository.findAll()) {
            if (product.getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    // productos que se estan quedando sin stock, para avisarle al vendedor
    public List<Inventory> getLowStock(int threshold) {
        List<Inventory> lowStock = new ArrayList<>();
        for (Inventory inventory : inventoryRepository.findAll()) {
            if (inventory.getQuantity() < threshold) {
                lowStock.add(inventory);
            }
        }
        return lowStock;
    }
}

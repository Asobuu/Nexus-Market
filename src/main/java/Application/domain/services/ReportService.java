package Application.domain.services;

import Application.domain.enums.OrderStatus;
import Application.domain.enums.ProductStatus;
import Application.domain.model.Inventory;
import Application.domain.model.Invoice;
import Application.domain.model.Order;
import Application.domain.model.Product;
import Application.domain.ports.out.InventoryRepositoryPort;
import Application.domain.ports.out.InvoiceRepositoryPort;
import Application.domain.ports.out.OrderRepositoryPort;
import Application.domain.ports.out.ProductRepositoryPort;

import java.util.ArrayList;
import java.util.List;

// consultas consolidadas para el administrador y el supervisor.
// aca no se modifica nada, solo se lee y se cuenta/suma
public class ReportService {

    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;

    public ReportService(InvoiceRepositoryPort invoiceRepositoryPort, OrderRepositoryPort orderRepositoryPort,
                          ProductRepositoryPort productRepositoryPort, InventoryRepositoryPort inventoryRepositoryPort) {
        this.invoiceRepositoryPort = invoiceRepositoryPort;
        this.orderRepositoryPort = orderRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
        this.inventoryRepositoryPort = inventoryRepositoryPort;
    }

    public double getTotalSales() {
        double total = 0;
        for (Invoice invoice : invoiceRepositoryPort.findAll()) {
            total += invoice.getTotalAmount();
        }
        return total;
    }

    public long countPendingOrders() {
        long count = 0;
        for (Order order : orderRepositoryPort.findAll()) {
            if (order.getStatus() != OrderStatus.DELIVERED) {
                count++;
            }
        }
        return count;
    }

    public long countProductsByStatus(ProductStatus status) {
        long count = 0;
        for (Product product : productRepositoryPort.findAll()) {
            if (product.getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    public List<Inventory> getLowStock(int threshold) {
        List<Inventory> lowStock = new ArrayList<>();
        for (Inventory inventory : inventoryRepositoryPort.findAll()) {
            if (inventory.getQuantity() < threshold) {
                lowStock.add(inventory);
            }
        }
        return lowStock;
    }
}

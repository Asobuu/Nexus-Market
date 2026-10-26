package Application.domain.ports.in;

import Application.domain.enums.ProductStatus;
import Application.domain.model.Inventory;

import java.util.List;

// requiere sesion con rol SUPERVISOR, todo es de solo lectura
public interface SupervisorPort {
    double getTotalSales();
    long countPendingOrders();
    long countProductsByStatus(ProductStatus status);
    List<Inventory> getLowStock(int threshold);
}

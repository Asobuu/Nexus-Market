package Application.adapters.rest.dto.response;

import Application.domain.model.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemResponse {

    private final Long id;
    private final Long productId;
    private final String productName;
    private final int quantity;
    private final double unitPrice;
    private final Long warehouseId;
    private final double subtotal;

    private OrderItemResponse(Long id, Long productId, String productName, int quantity, double unitPrice,
                               Long warehouseId, double subtotal) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.warehouseId = warehouseId;
        this.subtotal = subtotal;
    }

    public static OrderItemResponse from(OrderItem item) {
        Long warehouseId = item.getWarehouse() == null ? null : item.getWarehouse().getId();
        return new OrderItemResponse(item.getId(), item.getProduct().getId(), item.getProduct().getName(),
                item.getQuantity(), item.getUnitPrice(), warehouseId, item.getSubtotal());
    }
}

package Application.adapters.rest.dto.response;

import Application.domain.model.Inventory;
import lombok.Getter;

@Getter
public class InventoryResponse {

    private final Long id;
    private final Long productId;
    private final Long warehouseId;
    private final int quantity;

    private InventoryResponse(Long id, Long productId, Long warehouseId, int quantity) {
        this.id = id;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
    }

    public static InventoryResponse from(Inventory inventory) {
        return new InventoryResponse(inventory.getId(), inventory.getProduct().getId(),
                inventory.getWarehouse().getId(), inventory.getQuantity());
    }
}

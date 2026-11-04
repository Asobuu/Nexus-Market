package Application.adapters.rest.dto.response;

import Application.domain.model.Warehouse;
import lombok.Getter;

@Getter
public class WarehouseResponse {

    private final Long id;
    private final String name;
    private final AddressResponse location;
    private final Long ownerSellerId;

    private WarehouseResponse(Long id, String name, AddressResponse location, Long ownerSellerId) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.ownerSellerId = ownerSellerId;
    }

    public static WarehouseResponse from(Warehouse warehouse) {
        Long ownerId = warehouse.getOwner() == null ? null : warehouse.getOwner().getId();
        return new WarehouseResponse(warehouse.getId(), warehouse.getName(), AddressResponse.from(warehouse.getLocation()),
                ownerId);
    }
}

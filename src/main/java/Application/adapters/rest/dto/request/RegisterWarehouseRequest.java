package Application.adapters.rest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterWarehouseRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Valid
    @NotNull
    private AddressRequest location;

    // null = bodega del marketplace, abierta a cualquier vendedor
    // (ver Warehouse: "si owner es null, la bodega es del marketplace")
    private Long ownerSellerId;
}

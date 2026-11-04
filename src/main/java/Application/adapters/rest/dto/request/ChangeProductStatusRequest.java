package Application.adapters.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

// SellerPort solo expone suspender o discontinuar (no hay un "volver a
// publicar"), por eso se valida contra esos dos valores exactos aca
// mismo en vez de aceptar cualquier ProductStatus: asi un valor invalido
// se rechaza con 400 por Bean Validation, sin logica extra en el controller
@Getter
@Setter
public class ChangeProductStatusRequest {

    @NotBlank
    @Pattern(regexp = "SUSPENDED|DISCONTINUED", message = "debe ser SUSPENDED o DISCONTINUED")
    private String status;
}

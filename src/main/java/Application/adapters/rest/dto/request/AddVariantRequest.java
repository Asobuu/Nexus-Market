package Application.adapters.rest.dto.request;

import lombok.Getter;
import lombok.Setter;

// los tres campos son opcionales porque no todo producto varia en los
// tres a la vez (ver el comentario de ProductVariant): una camiseta
// puede variar solo en color y talla, por ejemplo
@Getter
@Setter
public class AddVariantRequest {

    private String color;
    private String size;
    private String model;
}

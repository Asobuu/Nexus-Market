package Application.model.user;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// direccion simple, la uso tanto en Buyer como en Warehouse
// es @Embeddable porque no tiene sentido que tenga su propia tabla con id,
// va metida dentro de la tabla del que la use
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String street;
    private String city;
    private String country;

    @Override
    public String toString() {
        return street + ", " + city + ", " + country;
    }
}

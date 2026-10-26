package Application.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// direccion simple, la uso tanto en Buyer como en Warehouse.
// es un value object: no tiene id propio y es inmutable, si una
// direccion cambia se reemplaza por una instancia nueva
@Getter
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

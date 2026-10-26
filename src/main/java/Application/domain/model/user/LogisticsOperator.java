package Application.domain.model.user;

import Application.domain.model.Warehouse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// se encarga de las bodegas y los envios.
// un operador puede estar en varias bodegas y una bodega puede tener
// varios operadores
@Getter
@NoArgsConstructor
public class LogisticsOperator extends User {

    private List<Warehouse> assignedWarehouses = new ArrayList<>();

    public LogisticsOperator(String fullName, String email) {
        super(fullName, email);
    }

    @Override
    public String getRoleName() {
        return "Logistics Operator";
    }

    public void assignWarehouse(Warehouse warehouse) {
        assignedWarehouses.add(warehouse);
    }
}

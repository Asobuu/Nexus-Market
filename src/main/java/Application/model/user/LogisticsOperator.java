package Application.model.user;

import Application.model.Warehouse;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// se encarga de las bodegas y los envios.
// un operador puede estar en varias bodegas y una bodega puede tener
// varios operadores, por eso es ManyToMany
@Entity
@DiscriminatorValue("LOGISTICS_OPERATOR")
@Getter
@Setter
@NoArgsConstructor
public class LogisticsOperator extends User {

    @ManyToMany
    @JoinTable(
            name = "operator_warehouses",
            joinColumns = @JoinColumn(name = "operator_id"),
            inverseJoinColumns = @JoinColumn(name = "warehouse_id")
    )
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

package Application.model.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// rol de solo consulta, no deberia modificar nada (eso se controla
// despues en los servicios/seguridad, aca solo esta el dato del rol)
@Entity
@DiscriminatorValue("SUPERVISOR")
@Getter
@Setter
@NoArgsConstructor
public class Supervisor extends User {

    public Supervisor(String fullName, String email) {
        super(fullName, email);
    }

    @Override
    public String getRoleName() {
        return "Supervisor";
    }
}

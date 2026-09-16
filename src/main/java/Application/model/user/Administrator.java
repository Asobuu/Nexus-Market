package Application.model.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// registra vendedores y bodegas, y aprueba reembolsos.
// no necesita atributos propios, con lo que hereda de User le basta
@Entity
@DiscriminatorValue("ADMINISTRATOR")
@Getter
@Setter
@NoArgsConstructor
public class Administrator extends User {

    public Administrator(String fullName, String email) {
        super(fullName, email);
    }

    @Override
    public String getRoleName() {
        return "Administrator";
    }
}

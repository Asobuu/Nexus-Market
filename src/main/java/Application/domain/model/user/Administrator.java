package Application.domain.model.user;

import lombok.NoArgsConstructor;

// registra vendedores, operadores logisticos y bodegas, y aprueba
// reembolsos. no necesita atributos propios, con lo que hereda de User
// le basta
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

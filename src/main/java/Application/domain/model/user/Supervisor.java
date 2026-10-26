package Application.domain.model.user;

import lombok.NoArgsConstructor;

// rol de solo consulta, no deberia modificar nada (eso se controla con
// el puerto de entrada que le corresponde, que solo expone reportes)
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

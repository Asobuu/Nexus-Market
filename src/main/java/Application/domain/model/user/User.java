package Application.domain.model.user;

import Application.domain.enums.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

// clase base de todos los que usan la plataforma. cada usuario es de
// una sola subclase a la vez (Buyer, Seller, etc), por eso no hay un
// campo "role" aparte, el rol lo dice el tipo de objeto.
// esta clase es dominio puro: no sabe nada de JPA ni de la base de datos,
// eso lo maneja la entidad espejo en la capa de persistencia
@Getter
@NoArgsConstructor
public abstract class User {

    private Long id;
    private String fullName;
    private String email;
    private UserStatus status;
    private String passwordHash;

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.status = UserStatus.ACTIVE;
    }

    // lo usa el adaptador de persistencia despues de guardar, para
    // completar el id que genero la base de datos
    public void assignId(Long id) {
        this.id = id;
    }

    // el hash lo calcula PasswordServicePort (bcrypt, por ejemplo), aca
    // solo se guarda el resultado. nunca se guarda la contraseña en texto plano
    public void assignPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    // cada subclase dice cual es su rol
    public abstract String getRoleName();

    public void activate() {
        status = UserStatus.ACTIVE;
    }

    public void block() {
        status = UserStatus.BLOCKED;
    }
}

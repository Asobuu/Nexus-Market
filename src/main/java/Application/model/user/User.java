package Application.model.user;

import Application.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// clase base de todos los que usan la plataforma.
// uso herencia de una sola tabla (todos los usuarios quedan en la
// tabla "users", con una columna "user_type" que dice el rol de cada uno).
// asi cada usuario solo puede ser de un tipo a la vez, porque en el
// codigo cada objeto es de una sola subclase (Buyer, Seller, etc).
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
@Getter
@Setter
@NoArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.status = UserStatus.ACTIVE;
    }

    // cada subclase dice cual es su rol
    public abstract String getRoleName();
}

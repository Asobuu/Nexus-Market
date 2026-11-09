package Application.adapters.persistence.jpa.entity;

import Application.domain.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.TableGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// cada tipo de usuario (Buyer, Seller, Administrator, Supervisor,
// LogisticsOperator) tiene su propia tabla, completa e independiente
// (ver @Table en cada subclase) - no hay una tabla "users" compartida
// ni columna discriminadora, asi que las clases de usuario no "chocan"
// entre si en la base de datos. El generador de ids (userIdGenerator)
// si se comparte entre las 5 tablas, para que nunca un Buyer y un
// Seller terminen con el mismo id aunque vivan en tablas distintas
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@TableGenerator(name = "userIdGenerator", table = "user_id_sequence", pkColumnName = "sequence_name",
        valueColumnName = "next_val", pkColumnValue = "user_id", allocationSize = 1)
@Getter
@Setter
@NoArgsConstructor
public abstract class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "userIdGenerator")
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(nullable = false)
    private String passwordHash;

    protected UserJpaEntity(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.status = UserStatus.ACTIVE;
    }
}

package Application.adapters.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "administrators")
@NoArgsConstructor
public class AdministratorJpaEntity extends UserJpaEntity {

    public AdministratorJpaEntity(String fullName, String email) {
        super(fullName, email);
    }
}

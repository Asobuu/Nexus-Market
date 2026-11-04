package Application.adapters.persistence.jpa.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ADMINISTRATOR")
@NoArgsConstructor
public class AdministratorJpaEntity extends UserJpaEntity {

    public AdministratorJpaEntity(String fullName, String email) {
        super(fullName, email);
    }
}

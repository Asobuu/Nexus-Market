package Application.adapters.persistence.jpa.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("SUPERVISOR")
@NoArgsConstructor
public class SupervisorJpaEntity extends UserJpaEntity {

    public SupervisorJpaEntity(String fullName, String email) {
        super(fullName, email);
    }
}

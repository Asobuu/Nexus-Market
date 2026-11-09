package Application.adapters.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supervisors")
@NoArgsConstructor
public class SupervisorJpaEntity extends UserJpaEntity {

    public SupervisorJpaEntity(String fullName, String email) {
        super(fullName, email);
    }
}

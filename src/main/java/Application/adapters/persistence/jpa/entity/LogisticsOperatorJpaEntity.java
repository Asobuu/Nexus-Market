package Application.adapters.persistence.jpa.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

// igual que en SellerJpaEntity: las bodegas asignadas no se mapean aca
@Entity
@DiscriminatorValue("LOGISTICS_OPERATOR")
@NoArgsConstructor
public class LogisticsOperatorJpaEntity extends UserJpaEntity {

    public LogisticsOperatorJpaEntity(String fullName, String email) {
        super(fullName, email);
    }
}

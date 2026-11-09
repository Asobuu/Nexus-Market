package Application.adapters.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

// igual que en SellerJpaEntity: las bodegas asignadas no se mapean aca
@Entity
@Table(name = "logistics_operators")
@NoArgsConstructor
public class LogisticsOperatorJpaEntity extends UserJpaEntity {

    public LogisticsOperatorJpaEntity(String fullName, String email) {
        super(fullName, email);
    }
}

package Application.adapters.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

// las bodegas y productos de un vendedor no se mapean aca: se consultan
// por su propio repositorio (WarehouseJpaRepository, ProductJpaRepository
// con findBySellerId), para no cargar colecciones completas en cada
// lectura de un Seller ni crear un ciclo de mapeo Seller -> Product ->
// Seller -> ... (ver SellerMapper)
@Entity
@Table(name = "sellers")
@NoArgsConstructor
public class SellerJpaEntity extends UserJpaEntity {

    public SellerJpaEntity(String fullName, String email) {
        super(fullName, email);
    }
}

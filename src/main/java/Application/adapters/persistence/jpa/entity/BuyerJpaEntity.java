package Application.adapters.persistence.jpa.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "buyers")
@Getter
@Setter
@NoArgsConstructor
public class BuyerJpaEntity extends UserJpaEntity {

    @Embedded
    private AddressEmbeddable address;

    // fetch = EAGER a proposito: por defecto @ElementCollection es LAZY, pero
    // JwtAuthenticationFilter recarga el Buyer en cada request autenticado
    // (UserRepositoryPort.findById) fuera de cualquier transaccion larga, y
    // la sesion de Hibernate de ese findById ya se cerro para cuando
    // BuyerMapper.toDomain() intenta leer esta lista -> LazyInitializationException
    // ("no session"). Al ser EAGER, Hibernate la trae de una vez junto con
    // el resto de la fila, sin depender de que la sesion siga abierta despues.
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "buyer_other_addresses", joinColumns = @JoinColumn(name = "buyer_id"))
    private List<AddressEmbeddable> otherAddresses = new ArrayList<>();

    public BuyerJpaEntity(String fullName, String email, AddressEmbeddable address) {
        super(fullName, email);
        this.address = address;
    }
}

package Application.adapters.persistence.jpa.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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

    @ElementCollection
    @CollectionTable(name = "buyer_other_addresses", joinColumns = @JoinColumn(name = "buyer_id"))
    private List<AddressEmbeddable> otherAddresses = new ArrayList<>();

    public BuyerJpaEntity(String fullName, String email, AddressEmbeddable address) {
        super(fullName, email);
        this.address = address;
    }
}

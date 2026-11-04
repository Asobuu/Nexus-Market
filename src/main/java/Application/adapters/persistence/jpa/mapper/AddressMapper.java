package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.AddressEmbeddable;
import Application.domain.model.user.Address;

public final class AddressMapper {

    private AddressMapper() {
    }

    public static Address toDomain(AddressEmbeddable embeddable) {
        if (embeddable == null) {
            return null;
        }
        return new Address(embeddable.getStreet(), embeddable.getCity(), embeddable.getCountry());
    }

    public static AddressEmbeddable toEmbeddable(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressEmbeddable(address.getStreet(), address.getCity(), address.getCountry());
    }
}

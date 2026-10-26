package Application.domain.ports.out;

import Application.domain.model.Warehouse;

import java.util.Optional;

public interface WarehouseRepositoryPort {
    Warehouse save(Warehouse warehouse);
    Optional<Warehouse> findById(Long id);
}

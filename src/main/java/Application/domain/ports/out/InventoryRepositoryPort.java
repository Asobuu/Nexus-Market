package Application.domain.ports.out;

import Application.domain.model.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryRepositoryPort {
    Inventory save(Inventory inventory);
    Optional<Inventory> findById(Long id);
    List<Inventory> findAll();
}

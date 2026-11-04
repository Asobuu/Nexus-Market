package Application.adapters.persistence.jpa.repository;

import Application.adapters.persistence.jpa.entity.OrderItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// no implementa ningun puerto de dominio: OrderItem no tiene un
// out port propio, viaja siempre dentro de Order. este repositorio es
// solo plomeria interna para que ShipmentRepositoryAdapter pueda marcar
// que OrderItemJpaEntity quedaron asignados a un Shipment
public interface OrderItemJpaRepository extends JpaRepository<OrderItemJpaEntity, Long> {
}

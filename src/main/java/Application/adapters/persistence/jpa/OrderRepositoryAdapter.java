package Application.adapters.persistence.jpa;

import Application.adapters.persistence.jpa.entity.BuyerJpaEntity;
import Application.adapters.persistence.jpa.entity.OrderJpaEntity;
import Application.adapters.persistence.jpa.entity.ProductJpaEntity;
import Application.adapters.persistence.jpa.entity.ShipmentJpaEntity;
import Application.adapters.persistence.jpa.entity.WarehouseJpaEntity;
import Application.adapters.persistence.jpa.mapper.OrderMapper;
import Application.adapters.persistence.jpa.repository.BuyerJpaRepository;
import Application.adapters.persistence.jpa.repository.OrderItemJpaRepository;
import Application.adapters.persistence.jpa.repository.OrderJpaRepository;
import Application.adapters.persistence.jpa.repository.ProductJpaRepository;
import Application.adapters.persistence.jpa.repository.WarehouseJpaRepository;
import Application.domain.model.Order;
import Application.domain.model.OrderItem;
import Application.domain.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final BuyerJpaRepository buyerJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final WarehouseJpaRepository warehouseJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository, BuyerJpaRepository buyerJpaRepository,
                                   ProductJpaRepository productJpaRepository,
                                   WarehouseJpaRepository warehouseJpaRepository,
                                   OrderItemJpaRepository orderItemJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
        this.buyerJpaRepository = buyerJpaRepository;
        this.productJpaRepository = productJpaRepository;
        this.warehouseJpaRepository = warehouseJpaRepository;
        this.orderItemJpaRepository = orderItemJpaRepository;
    }

    @Override
    public Order save(Order order) {
        BuyerJpaEntity buyerEntity = buyerJpaRepository.getReferenceById(order.getBuyer().getId());

        Map<Long, ProductJpaEntity> productEntitiesByProductId = new HashMap<>();
        Map<Long, WarehouseJpaEntity> warehouseEntitiesByWarehouseId = new HashMap<>();
        // si el item ya existia (tiene id), hay que leer que Shipment tenia
        // asignado en la BD para no perderlo: OrderMapper.toEntity reconstruye
        // cada OrderItemJpaEntity desde cero y el dominio no conoce el envio
        // (ver comentario en OrderMapper.toEntity)
        Map<Long, ShipmentJpaEntity> existingShipmentByItemId = new HashMap<>();
        for (OrderItem item : order.getItems()) {
            productEntitiesByProductId.computeIfAbsent(item.getProduct().getId(),
                    productJpaRepository::getReferenceById);
            if (item.getWarehouse() != null) {
                warehouseEntitiesByWarehouseId.computeIfAbsent(item.getWarehouse().getId(),
                        warehouseJpaRepository::getReferenceById);
            }
            if (item.getId() != null) {
                orderItemJpaRepository.findById(item.getId())
                        .ifPresent(existingItem -> existingShipmentByItemId.put(item.getId(),
                                existingItem.getShipment()));
            }
        }

        OrderJpaEntity entity = orderJpaRepository.save(
                OrderMapper.toEntity(order, buyerEntity, productEntitiesByProductId, warehouseEntitiesByWarehouseId,
                        existingShipmentByItemId));
        return OrderMapper.toDomain(entity);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(OrderMapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return orderJpaRepository.findAll().stream().map(OrderMapper::toDomain).toList();
    }

    @Override
    public List<Order> findByBuyerId(Long buyerId) {
        return orderJpaRepository.findByBuyerId(buyerId).stream().map(OrderMapper::toDomain).toList();
    }
}

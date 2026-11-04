package Application.adapters.persistence.jpa;

import Application.adapters.persistence.jpa.entity.LogisticsOperatorJpaEntity;
import Application.adapters.persistence.jpa.entity.OrderItemJpaEntity;
import Application.adapters.persistence.jpa.entity.OrderJpaEntity;
import Application.adapters.persistence.jpa.entity.ShipmentJpaEntity;
import Application.adapters.persistence.jpa.entity.WarehouseJpaEntity;
import Application.adapters.persistence.jpa.mapper.ShipmentMapper;
import Application.adapters.persistence.jpa.repository.LogisticsOperatorJpaRepository;
import Application.adapters.persistence.jpa.repository.OrderItemJpaRepository;
import Application.adapters.persistence.jpa.repository.OrderJpaRepository;
import Application.adapters.persistence.jpa.repository.ShipmentJpaRepository;
import Application.adapters.persistence.jpa.repository.WarehouseJpaRepository;
import Application.domain.model.OrderItem;
import Application.domain.model.Shipment;
import Application.domain.ports.out.ShipmentRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class ShipmentRepositoryAdapter implements ShipmentRepositoryPort {

    private final ShipmentJpaRepository shipmentJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final WarehouseJpaRepository warehouseJpaRepository;
    private final LogisticsOperatorJpaRepository logisticsOperatorJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;

    public ShipmentRepositoryAdapter(ShipmentJpaRepository shipmentJpaRepository,
                                      OrderJpaRepository orderJpaRepository,
                                      WarehouseJpaRepository warehouseJpaRepository,
                                      LogisticsOperatorJpaRepository logisticsOperatorJpaRepository,
                                      OrderItemJpaRepository orderItemJpaRepository) {
        this.shipmentJpaRepository = shipmentJpaRepository;
        this.orderJpaRepository = orderJpaRepository;
        this.warehouseJpaRepository = warehouseJpaRepository;
        this.logisticsOperatorJpaRepository = logisticsOperatorJpaRepository;
        this.orderItemJpaRepository = orderItemJpaRepository;
    }

    @Override
    @Transactional
    public Shipment save(Shipment shipment) {
        OrderJpaEntity orderEntity = orderJpaRepository.getReferenceById(shipment.getOrder().getId());
        WarehouseJpaEntity originWarehouseEntity =
                warehouseJpaRepository.getReferenceById(shipment.getOriginWarehouse().getId());
        LogisticsOperatorJpaEntity operatorEntity = shipment.getOperator() == null
                ? null
                : logisticsOperatorJpaRepository.getReferenceById(shipment.getOperator().getId());

        ShipmentJpaEntity entity = shipmentJpaRepository.save(
                ShipmentMapper.toEntity(shipment, orderEntity, originWarehouseEntity, operatorEntity));

        // los items del envio ya existen como OrderItemJpaEntity (son items de
        // un pedido ya guardado); aca solo se les marca a que envio quedaron
        // asignados, no se vuelven a crear
        for (OrderItem item : shipment.getItems()) {
            OrderItemJpaEntity itemEntity = orderItemJpaRepository.findById(item.getId())
                    .orElseThrow(() -> new IllegalStateException(
                            "el item de pedido " + item.getId() + " no existe, no se puede asignar al envio"));
            itemEntity.setShipment(entity);
            orderItemJpaRepository.save(itemEntity);
        }

        shipment.assignId(entity.getId());
        return shipment;
    }

    @Override
    public Optional<Shipment> findById(Long id) {
        return shipmentJpaRepository.findById(id).map(ShipmentMapper::toDomain);
    }

    @Override
    public List<Shipment> findByOrderId(Long orderId) {
        return shipmentJpaRepository.findByOrderId(orderId).stream().map(ShipmentMapper::toDomain).toList();
    }
}

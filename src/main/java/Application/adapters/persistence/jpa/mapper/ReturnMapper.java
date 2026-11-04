package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.OrderJpaEntity;
import Application.adapters.persistence.jpa.entity.ProductJpaEntity;
import Application.adapters.persistence.jpa.entity.ReturnJpaEntity;
import Application.domain.model.Return;

public final class ReturnMapper {

    private ReturnMapper() {
    }

    public static Return toDomain(ReturnJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        Return returnRequest = new Return(OrderMapper.toDomain(entity.getOrder()),
                ProductMapper.toDomain(entity.getProduct()), entity.getReason());
        returnRequest.assignId(entity.getId());
        returnRequest.assignRefundAmount(entity.getRefundAmount());
        // el constructor siempre arranca en REQUESTED; se restaura el estado
        // real guardado, sin pasar por las validaciones de transicion
        // (ver Return.assignStatus)
        returnRequest.assignStatus(entity.getStatus());
        return returnRequest;
    }

    public static ReturnJpaEntity toEntity(Return returnRequest, OrderJpaEntity orderEntity,
                                            ProductJpaEntity productEntity) {
        ReturnJpaEntity entity = new ReturnJpaEntity(orderEntity, productEntity, returnRequest.getReason(),
                returnRequest.getStatus());
        entity.setId(returnRequest.getId());
        entity.setRefundAmount(returnRequest.getRefundAmount());
        return entity;
    }
}

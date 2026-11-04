package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.LogisticsOperatorJpaEntity;
import Application.domain.enums.UserStatus;
import Application.domain.model.user.LogisticsOperator;

public final class LogisticsOperatorMapper {

    private LogisticsOperatorMapper() {
    }

    public static LogisticsOperator toDomain(LogisticsOperatorJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        LogisticsOperator operator = new LogisticsOperator(entity.getFullName(), entity.getEmail());
        operator.assignId(entity.getId());
        operator.assignPasswordHash(entity.getPasswordHash());
        if (entity.getStatus() == UserStatus.BLOCKED) {
            operator.block();
        }
        return operator;
    }

    public static LogisticsOperatorJpaEntity toEntity(LogisticsOperator operator) {
        LogisticsOperatorJpaEntity entity =
                new LogisticsOperatorJpaEntity(operator.getFullName(), operator.getEmail());
        entity.setId(operator.getId());
        entity.setStatus(operator.getStatus());
        entity.setPasswordHash(operator.getPasswordHash());
        return entity;
    }
}

package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.SupervisorJpaEntity;
import Application.domain.enums.UserStatus;
import Application.domain.model.user.Supervisor;

public final class SupervisorMapper {

    private SupervisorMapper() {
    }

    public static Supervisor toDomain(SupervisorJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        Supervisor supervisor = new Supervisor(entity.getFullName(), entity.getEmail());
        supervisor.assignId(entity.getId());
        supervisor.assignPasswordHash(entity.getPasswordHash());
        if (entity.getStatus() == UserStatus.BLOCKED) {
            supervisor.block();
        }
        return supervisor;
    }

    public static SupervisorJpaEntity toEntity(Supervisor supervisor) {
        SupervisorJpaEntity entity = new SupervisorJpaEntity(supervisor.getFullName(), supervisor.getEmail());
        entity.setId(supervisor.getId());
        entity.setStatus(supervisor.getStatus());
        entity.setPasswordHash(supervisor.getPasswordHash());
        return entity;
    }
}

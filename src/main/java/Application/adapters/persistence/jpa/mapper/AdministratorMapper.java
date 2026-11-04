package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.AdministratorJpaEntity;
import Application.domain.enums.UserStatus;
import Application.domain.model.user.Administrator;

public final class AdministratorMapper {

    private AdministratorMapper() {
    }

    public static Administrator toDomain(AdministratorJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        Administrator administrator = new Administrator(entity.getFullName(), entity.getEmail());
        administrator.assignId(entity.getId());
        administrator.assignPasswordHash(entity.getPasswordHash());
        if (entity.getStatus() == UserStatus.BLOCKED) {
            administrator.block();
        }
        return administrator;
    }

    public static AdministratorJpaEntity toEntity(Administrator administrator) {
        AdministratorJpaEntity entity =
                new AdministratorJpaEntity(administrator.getFullName(), administrator.getEmail());
        entity.setId(administrator.getId());
        entity.setStatus(administrator.getStatus());
        entity.setPasswordHash(administrator.getPasswordHash());
        return entity;
    }
}

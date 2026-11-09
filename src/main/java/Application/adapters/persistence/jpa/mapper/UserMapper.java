package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.AdministratorJpaEntity;
import Application.adapters.persistence.jpa.entity.BuyerJpaEntity;
import Application.adapters.persistence.jpa.entity.LogisticsOperatorJpaEntity;
import Application.adapters.persistence.jpa.entity.SellerJpaEntity;
import Application.adapters.persistence.jpa.entity.SupervisorJpaEntity;
import Application.adapters.persistence.jpa.entity.UserJpaEntity;
import Application.domain.model.user.User;

// un UserJpaEntity siempre es en realidad una de sus subclases (cada
// una con su propia tabla independiente - buyers, sellers,
// administrators, supervisors, logistics_operators -, ver
// UserJpaEntity); aca se decide a que mapper especifico delegar segun
// el tipo real del objeto
public final class UserMapper {

    private UserMapper() {
    }

    public static User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        if (entity instanceof BuyerJpaEntity buyer) {
            return BuyerMapper.toDomain(buyer);
        }
        if (entity instanceof SellerJpaEntity seller) {
            return SellerMapper.toDomain(seller);
        }
        if (entity instanceof LogisticsOperatorJpaEntity operator) {
            return LogisticsOperatorMapper.toDomain(operator);
        }
        if (entity instanceof AdministratorJpaEntity administrator) {
            return AdministratorMapper.toDomain(administrator);
        }
        if (entity instanceof SupervisorJpaEntity supervisor) {
            return SupervisorMapper.toDomain(supervisor);
        }
        throw new IllegalStateException("tipo de usuario desconocido: " + entity.getClass());
    }
}

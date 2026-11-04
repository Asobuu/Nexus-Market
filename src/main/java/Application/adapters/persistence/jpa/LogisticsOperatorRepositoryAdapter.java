package Application.adapters.persistence.jpa;

import Application.adapters.persistence.jpa.entity.LogisticsOperatorJpaEntity;
import Application.adapters.persistence.jpa.mapper.LogisticsOperatorMapper;
import Application.adapters.persistence.jpa.repository.LogisticsOperatorJpaRepository;
import Application.domain.model.user.LogisticsOperator;
import Application.domain.ports.out.LogisticsOperatorRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LogisticsOperatorRepositoryAdapter implements LogisticsOperatorRepositoryPort {

    private final LogisticsOperatorJpaRepository logisticsOperatorJpaRepository;

    public LogisticsOperatorRepositoryAdapter(LogisticsOperatorJpaRepository logisticsOperatorJpaRepository) {
        this.logisticsOperatorJpaRepository = logisticsOperatorJpaRepository;
    }

    @Override
    public LogisticsOperator save(LogisticsOperator operator) {
        LogisticsOperatorJpaEntity entity =
                logisticsOperatorJpaRepository.save(LogisticsOperatorMapper.toEntity(operator));
        return LogisticsOperatorMapper.toDomain(entity);
    }

    @Override
    public Optional<LogisticsOperator> findById(Long id) {
        return logisticsOperatorJpaRepository.findById(id).map(LogisticsOperatorMapper::toDomain);
    }
}

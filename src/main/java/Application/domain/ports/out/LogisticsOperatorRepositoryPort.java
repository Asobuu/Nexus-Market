package Application.domain.ports.out;

import Application.domain.model.user.LogisticsOperator;

import java.util.Optional;

public interface LogisticsOperatorRepositoryPort {
    LogisticsOperator save(LogisticsOperator operator);
    Optional<LogisticsOperator> findById(Long id);
}

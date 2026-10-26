package Application.domain.ports.out;

import Application.domain.model.Return;

import java.util.Optional;

public interface ReturnRepositoryPort {
    Return save(Return returnRequest);
    Optional<Return> findById(Long id);
}

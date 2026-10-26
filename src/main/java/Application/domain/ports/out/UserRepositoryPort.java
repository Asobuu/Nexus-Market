package Application.domain.ports.out;

import Application.domain.model.user.User;

import java.util.Optional;

// consultas que aplican a cualquier tipo de usuario, sin importar el rol
public interface UserRepositoryPort {
    boolean existsByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}

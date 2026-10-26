package Application.domain.ports.out;

import Application.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    List<Order> findByBuyerId(Long buyerId);
}

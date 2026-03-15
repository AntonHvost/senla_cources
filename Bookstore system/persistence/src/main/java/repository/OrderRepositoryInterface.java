package repository;

import domain.model.impl.Order;

import java.util.Optional;

public interface OrderRepositoryInterface extends Repository<Order,Long> {
    Optional<Order> findOrderWithConsumerAndOrderItems(Long orderId);
}

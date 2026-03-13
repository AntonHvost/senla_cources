package repository.impl;

import domain.model.impl.Order;
import org.springframework.stereotype.Repository;
import repository.BaseRepository;
import repository.OrderRepositoryInterface;

import java.util.Optional;


@Repository
public class OrderRepository extends BaseRepository<Order, Long> implements OrderRepositoryInterface {

    OrderRepository() {
        super(Order.class);
    }

    @Override
    public Optional<Order> findOrderWithConsumerAndOrderItems(Long orderId) {
        try {

            Order order = em
                    .createQuery("select o from Order o " +
                            "left join fetch o.consumer " +
                            "left join fetch o.orderItemsList oi " +
                            "left join fetch oi.book " +
                            "where o.id = :orderId", Order.class)
                    .setParameter("orderId", orderId)
                    .getSingleResult();

            return Optional.of(order);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

}

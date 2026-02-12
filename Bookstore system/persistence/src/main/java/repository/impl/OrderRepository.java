package repository.impl;

import domain.model.impl.Order;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import repository.BaseRepository;
import util.HibernateUtil;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Optional;


@Repository
public class OrderRepository extends BaseRepository<Order, Long> {

    OrderRepository() {
        super(Order.class);
    }

    public Optional<Order> findOrderWithConsumerAndOrderItems(Long orderId) {
        Session session = HibernateUtil.getSession();
        try {

            Order order = session
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

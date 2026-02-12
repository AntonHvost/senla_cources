package repository.impl;

import domain.model.impl.Order;
import org.springframework.stereotype.Repository;
import repository.BaseRepository;

@Repository
public class OrderRepository extends BaseRepository<Order, Long> {

    OrderRepository() {
        super(Order.class);
    }

}

package repository.impl;


import di.annotation.Component;
import domain.model.impl.Order;
import repository.BaseRepository;

@Component
public class OrderRepository extends BaseRepository<Order, Long> {

    OrderRepository() {
        super(Order.class);
    }

}

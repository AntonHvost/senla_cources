package repository.impl;

import di.annotation.Component;
import domain.model.impl.OrderItem;
import repository.BaseRepository;

@Component
public class OrderItemRepository extends BaseRepository<OrderItem, Long> {

   public OrderItemRepository() {
       super(OrderItem.class);
   }
}

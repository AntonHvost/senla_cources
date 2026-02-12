package repository.impl;

import domain.model.impl.OrderItem;
import org.springframework.stereotype.Repository;
import repository.BaseRepository;

@Repository
public class OrderItemRepository extends BaseRepository<OrderItem, Long> {

   public OrderItemRepository() {
       super(OrderItem.class);
   }
}

package bookstore_system.domain.repository;

import bookstore_system.di.annotation.Component;
import bookstore_system.domain.model.Book;
import bookstore_system.domain.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrderRepository implements Repository<Order> {
    private List<Order> orderList;
    private Long nextOrderId;
    private Long nextOrderItemId;

    public OrderRepository () {
        orderList = new ArrayList<>();
        nextOrderId = 1L;
        nextOrderItemId = 1L;
    }

    @Override
    public List<Order> findAll() {
        return orderList;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderList.stream().filter(o -> o.getId().equals(id)).findAny();
    }

    public void replaceAll(List<Order> orderList, Long nextOrderId, Long nextOrderItemId) {
        this.orderList = orderList;
        this.nextOrderId = nextOrderId;
        this.nextOrderItemId = nextOrderItemId;
    }

    @Override
    public void save(Order order) {
        orderList.add(order);
    }

    @Override
    public void update(Order order) {
        for (Order o : orderList){
            if (o.getId().equals(order.getId())) {
                orderList.set(orderList.indexOf(o), order);
                return;
            }
        }
    }

    @Override
    public Long generateNextId() {
        return nextOrderId++;
    }

    public Long generateNextItemId() {
        return nextOrderItemId++;
    }

    @Override
    public Long getNextId() {
        return nextOrderId;
    }

    public Long getNextItemId() {
        return nextOrderItemId;
    }

    @Override
    public void setNextId(Long nextId) {
        this.nextOrderId = nextId;
    }
    public void setNextItemId(Long nextId) {
        this.nextOrderItemId = nextId;
    }
}

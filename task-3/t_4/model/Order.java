package t_4.model;

import t_4.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private long id;
    private List<OrderItem> orderItemsList;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;

    public Order(long id){
        this.id = id;
        this.orderDate = LocalDateTime.now();
        this.orderStatus = OrderStatus.NEW;
        this.orderItemsList = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void addItem(OrderItem item){
        orderItemsList.add(item);
    }

    public List<OrderItem> getOrderItemsList() {
        return orderItemsList;
    }

    public boolean canBeCompleted() {
        return orderStatus == OrderStatus.IN_PROCESS;
    }
}

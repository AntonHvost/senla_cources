package t1.model;

import t1.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private long id;
    private List<OrderItem> orderItemsList;
    private Consumer consumer;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;

    public Order(long id, Consumer consumer){
        this.id = id;
        this.orderDate = LocalDateTime.now();
        this.orderStatus = OrderStatus.NEW;
        this.consumer = consumer;
        this.orderItemsList = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
    }

    public long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderItem> getOrderItemsList() {
        return orderItemsList;
    }

    public void addItem(OrderItem item){
        orderItemsList.add(item);
    }

    public boolean canBeCompleted() {
        return orderStatus == OrderStatus.IN_PROCESS;
    }

    public void calculateTotalPrice() {
        for (OrderItem i : orderItemsList){
            totalPrice = totalPrice.add(i.getBook().getPrice());
        }
    }
}

package t1.model;

import t1.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Order {
    private long id;
    private List<OrderItem> orderItemsList;
    private Consumer consumer;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;

    public Order(long id, Consumer consumer) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
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

    public LocalDateTime getCreatedOrderDate() {
        return createdAt;
    }

    public LocalDateTime getCompletedOrderDate() {
        return completedAt;
    }

    public void setCompletedAtDate(LocalDateTime completedAt) {
        this.completedAt = completedAt;
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

    public void addItem(OrderItem item) {
        orderItemsList.add(item);
    }

    public boolean canBeCompleted() {
        return orderStatus == OrderStatus.IN_PROCESS;
    }

    public void calculateTotalPrice() {
        this.totalPrice = getOrderItemsList().stream()
                .map(orderItem -> {
                    BigDecimal bookPrice = orderItem.getBook().getPrice();
                    return bookPrice.multiply(BigDecimal.valueOf(orderItem.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

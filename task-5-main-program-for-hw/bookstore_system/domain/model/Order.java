package bookstore_system.domain.model;

import bookstore_system.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private List<OrderItem> orderItemsList;
    private Long consumerId;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;

    public Order(Long id, Long consumerId) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.orderStatus = OrderStatus.NEW;
        this.consumerId = consumerId;
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

    public Long getConsumerId() {
        return consumerId;
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

package bookstore_system.domain.model;

import bookstore_system.enums.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order implements Indedifiable {
    private static long nextId = 1;
    private Long id;
    private Long consumerId;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private List<OrderItem> orderItemsList;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;

    public Order() {
        this.createdAt = LocalDateTime.now();
        this.orderStatus = OrderStatus.NEW;
        this.orderItemsList = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
    }

    public Order(Long consumerId) {
        this.id = nextId++;
        this.createdAt = LocalDateTime.now();
        this.orderStatus = OrderStatus.NEW;
        this.consumerId = consumerId;
        this.orderItemsList = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
    }

    @Override
    public Long getId() {
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


    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public List<OrderItem> getOrderItemsList() {
        return orderItemsList;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderItemsList(List<OrderItem> orderItemsList) {
        this.orderItemsList = orderItemsList;
    }

    public void setCreatedOrder(LocalDateTime createdOrderDate) {
        this.createdAt = createdOrderDate;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public void setCompletedAtDate(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void addItem(OrderItem item) {
        orderItemsList.add(item);
    }

    public boolean canBeCompleted() {
        return orderStatus == OrderStatus.IN_PROCESS;
    }

    public static void ensureId(long id) {
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

}

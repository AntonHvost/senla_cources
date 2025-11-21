package bookstore_system.dto;

import bookstore_system.domain.OrderItem;
import bookstore_system.enums.OrderStatus;
import bookstore_system.domain.Consumer;
import bookstore_system.domain.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrderSummary {
    private long id;
    private Consumer consumer;
    private LocalDateTime createdOrderDate;
    private LocalDateTime completedOrderDate;
    private List<OrderItem> orderItemList;
    private BigDecimal price;
    private OrderStatus status;

    public OrderSummary(Order order) {
        this.id = order.getId();
        this.consumer = order.getConsumer();
        this.createdOrderDate = order.getCreatedOrderDate();
        this.completedOrderDate = order.getCompletedOrderDate();
        this.price = order.getTotalPrice();
        this.status = order.getOrderStatus();
        this.orderItemList = order.getOrderItemsList();
    }

    public long getId() {
        return id;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public LocalDateTime getCreatedOrderDate() {
        return createdOrderDate;
    }

    public LocalDateTime getCompletedOrderDate() {
        return completedOrderDate;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderStatus getStatus() {
        return status;
    }
}

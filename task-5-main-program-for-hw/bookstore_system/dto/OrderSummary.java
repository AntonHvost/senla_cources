package bookstore_system.dto;

import bookstore_system.domain.model.OrderItem;
import bookstore_system.domain.service.ConsumerService;
import bookstore_system.enums.OrderStatus;
import bookstore_system.domain.model.Consumer;
import bookstore_system.domain.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderSummary {
    private long id;
    private Consumer consumer;
    private LocalDateTime createdOrderDate;
    private LocalDateTime completedOrderDate;
    private List<OrderItemSummary> orderItemList;
    private BigDecimal price;
    private OrderStatus status;

    public OrderSummary(Order order, Consumer consumer, List<OrderItemSummary> orderItem) {
        this.id = order.getId();
        this.consumer = consumer;
        this.createdOrderDate = order.getCreatedOrderDate();
        this.completedOrderDate = order.getCompletedOrderDate();
        this.price = order.getTotalPrice();
        this.status = order.getOrderStatus();
        this.orderItemList = orderItem;
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

    public List<OrderItemSummary> getOrderItemList() {
        return orderItemList;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderStatus getStatus() {
        return status;
    }
}

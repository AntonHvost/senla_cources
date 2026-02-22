package dto;

import enums.OrderStatus;
import domain.model.impl.Consumer;
import domain.model.impl.Order;

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
        this.createdOrderDate = order.getCreatedAtDate();
        this.completedOrderDate = order.getCompletedAtDate();
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

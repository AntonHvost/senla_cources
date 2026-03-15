package dto.response;

import enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {
    private Long id;
    private ConsumerResponseDto consumer;
    private LocalDateTime createdOrderDate;
    private LocalDateTime completedOrderDate;
    private List<OrderItemResponseDto> orderItems;
    private BigDecimal totalPrice;
    private OrderStatus status;

    public OrderResponseDto() {
    }

    public OrderResponseDto(Long id, ConsumerResponseDto consumer, LocalDateTime createdOrderDate,
                            LocalDateTime completedOrderDate, List<OrderItemResponseDto> orderItems,
                            BigDecimal totalPrice, OrderStatus status) {
        this.id = id;
        this.consumer = consumer;
        this.createdOrderDate = createdOrderDate;
        this.completedOrderDate = completedOrderDate;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConsumerResponseDto getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerResponseDto consumer) {
        this.consumer = consumer;
    }

    public LocalDateTime getCreatedOrderDate() {
        return createdOrderDate;
    }

    public void setCreatedOrderDate(LocalDateTime createdOrderDate) {
        this.createdOrderDate = createdOrderDate;
    }

    public LocalDateTime getCompletedOrderDate() {
        return completedOrderDate;
    }

    public void setCompletedOrderDate(LocalDateTime completedOrderDate) {
        this.completedOrderDate = completedOrderDate;
    }

    public List<OrderItemResponseDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponseDto> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

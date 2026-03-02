package domain.model;

import domain.model.impl.Identifiable;
import enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect
public class Order implements Identifiable {
    private Long id;
    private Long consumerId;
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonIgnore
    public LocalDateTime getCreatedAtDate() {
        return createdAt;
    }
    @JsonIgnore
    public LocalDateTime getCompletedAtDate() {
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

    @JsonIgnore
    public void setOrderItemsList(List<OrderItem> orderItemsList) {
        this.orderItemsList = orderItemsList;
    }
    @JsonIgnore
    public void setCreatedAtDate(LocalDateTime createdOrderDate) {
        this.createdAt = createdOrderDate;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }
    @JsonIgnore
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

}

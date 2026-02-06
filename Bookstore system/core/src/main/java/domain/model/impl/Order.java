package domain.model.impl;

import domain.model.Identifiable;
import enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect
@Entity
@Table(name = "order")
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
    @Id
    public Long getId() {
        return id;
    }

    @Column(name = "status")
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
    @JsonIgnore
    @Column(name = "created_at")
    public LocalDateTime getCreatedAtDate() {
        return createdAt;
    }
    @JsonIgnore
    @Column(name = "completed_at")
    public LocalDateTime getCompletedAtDate() {
        return completedAt;
    }

    @Column(name = "total_price")
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    public List<OrderItem> getOrderItemsList() {
        return orderItemsList;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "consumer_id")
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

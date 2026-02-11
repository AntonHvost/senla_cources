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
import java.util.Locale;

@JsonAutoDetect
@Entity
@Table(name = "`order`")
public class Order implements Identifiable {
    private Long id;
    private Consumer consumer;
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

    public Order(Consumer consumer) {
        this.createdAt = LocalDateTime.now();
        this.orderStatus = OrderStatus.NEW;
        this.consumer = consumer;
        this.orderItemsList = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(name = "order_seq",
    sequenceName = "order_id_seq",
    allocationSize = 1)
    public Long getId() {
        return id;
    }

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)",name = "status")
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


    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<OrderItem> getOrderItemsList() {
        return orderItemsList;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id", nullable = false)
    public Consumer getConsumer() {
        return consumer;
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

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
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

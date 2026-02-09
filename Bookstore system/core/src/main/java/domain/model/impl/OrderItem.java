package domain.model.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import domain.model.Identifiable;

import javax.persistence.*;

@JsonAutoDetect
@Entity
@Table(name = "order_item")
public class OrderItem implements Identifiable {
    private Long id;
    private Long orderId;
    private Long bookId;
    private int quantity;


    public OrderItem() {}

    public OrderItem(Long id,Long orderId, Long bookId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
    //@ManyToOne(cascade = CascadeType.ALL)
    @Column(name = "order_id")
    public Long getOrderId() {
        return orderId;
    }

    //@ManyToOne(cascade = CascadeType.ALL)
    @Column(name = "book_id")
    public Long getBookId() {
        return bookId;
    }

    @Column(name = "quantity")
    public int getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

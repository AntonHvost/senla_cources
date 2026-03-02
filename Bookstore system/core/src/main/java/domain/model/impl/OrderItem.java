package domain.model.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import domain.model.Identifiable;

import javax.persistence.*;

@JsonAutoDetect
@Entity
@Table(name = "order_item")
public class OrderItem implements Identifiable {
    private Long id;
    private Order order;
    private Book book;
    private int quantity;


    public OrderItem() {}

    public OrderItem(Long id,Order order, Book book, int quantity) {
        this.id = id;
        this.order = order;
        this.book = book;
        this.quantity = quantity;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_seq")
    @SequenceGenerator(name = "order_item_seq",
            sequenceName = "order_item_id_seq",
            allocationSize = 1)
    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    public Order getOrder() {
        return order;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    public Book getBook() {
        return book;
    }

    @Column(name = "quantity")
    public int getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

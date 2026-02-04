package dto;

import domain.model.impl.Book;
import domain.model.impl.OrderItem;

public class OrderItemSummary {
    Long orderId;
    Book book;
    int quantity;

    public OrderItemSummary(OrderItem item, Book book) {
        this.orderId = item.getOrderId();
        this.book = book;
        this.quantity = item.getQuantity();
    }

    public Long getOrderId() {
        return orderId;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }
}

package bookstore_system.dto;

import bookstore_system.domain.model.Book;
import bookstore_system.domain.model.OrderItem;

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

package dto;

import domain.model.impl.Book;
import domain.model.impl.OrderItem;

public class OrderItemSummary {
    Long orderId;
    Book book;
    int quantity;

    public OrderItemSummary(OrderItem item) {
        this.orderId = item.getOrder().getId();
        this.book = item.getBook();
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

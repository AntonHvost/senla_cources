package bookstore_system.domain.model;

import java.util.Optional;

public class OrderItem {
    private Long orderId;
    private Long bookId;
    private int quantity;

    public OrderItem(Long id, Long bookId, int quantity) {
        this.orderId = id;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }
    public Long getBookId() {
        return bookId;
    }

    public int getQuantity() {
        return quantity;
    }
}

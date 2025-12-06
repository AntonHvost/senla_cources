package bookstore_system.domain.model;

import bookstore_system.io.csv.CsvConverter;

import java.util.Optional;

public class OrderItem implements Indedifiable {
    private static long nextId = 1;
    private Long id;
    private Long orderId;
    private Long bookId;
    private int quantity;


    public OrderItem() {
        this.id = 0L;
    }

    public OrderItem(Long orderId, Long bookId, int quantity) {
        this.id = nextId++;
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    @Override
    public Long getId() {
        return id;
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

    public static void ensureId(long id) {
        if (id >= nextId) {
            nextId = id + 1;
        }
    }
}

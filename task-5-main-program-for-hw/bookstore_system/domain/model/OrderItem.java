package bookstore_system.domain.model;

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
}

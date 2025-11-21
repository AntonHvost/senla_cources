package bookstore_system.domain;

public class OrderItem {
    private Long id;
    private Book book;
    private int quantity;

    public OrderItem(long id, Book book, int quantity) {
        this.id = id;
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }
}

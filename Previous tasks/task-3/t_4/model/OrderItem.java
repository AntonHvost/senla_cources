package t_4.model;

import java.math.BigDecimal;

public class OrderItem {
    private Long id;
    private Book book;

    public OrderItem(long id, Book book) {
        this.id = id;
        this.book = book;
    }
}

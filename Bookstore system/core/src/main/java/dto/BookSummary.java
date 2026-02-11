package dto;

import enums.BookStatus;
import domain.model.impl.Book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookSummary {
    private long id;
    private String title;
    private String author;
    private String description;
    private LocalDate publishDate;
    private LocalDateTime deliveryDate;
    private BigDecimal price;
    private BookStatus status;

    public BookSummary (Book book, LocalDateTime deliveryDate) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.description = book.getDescription();
        this.publishDate = book.getPublishDate();
        this.deliveryDate = deliveryDate;
        this.price = book.getPrice();
        this.status = book.getStatus();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BookStatus getStatus() {
        return status;
    }
}

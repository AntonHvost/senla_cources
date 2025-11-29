package bookstore_system.domain.model;

import bookstore_system.enums.BookStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Book implements Indedifiable {
    private Long id;
    private String title;
    private String author;
    private String description;
    private LocalDate publishDate;
    private LocalDateTime deliveryDate;
    private BigDecimal price;
    private BookStatus status;

    public Book() {}
    public Book (Long id, String title, String author, String description, LocalDate publishDate, BigDecimal price, BookStatus status){
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.publishDate = publishDate;
        this.deliveryDate = LocalDateTime.now();
        this.price = price;
        this.status = status;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public boolean isAvaible(){
        return status == BookStatus.AVAILABLE;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}

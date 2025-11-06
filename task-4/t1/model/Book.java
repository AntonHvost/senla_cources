package t1.model;

import t1.enums.BookStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Book {
    private long id;
    private String title;
    private String author;
    private String description;
    private LocalDate publishDate;
    private BigDecimal price;
    private BookStatus status;

    public Book (long id, String title, String author, String description, LocalDate publishDate, BigDecimal price, BookStatus status){
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.publishDate = publishDate;
        this.price = price;
        this.status = status;
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

    public BigDecimal getPrice() {
        return price;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public boolean isAvaible(){
        return status == BookStatus.AVAILABLE;
    }
}

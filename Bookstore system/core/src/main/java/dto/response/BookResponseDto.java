package dto.response;

import enums.BookStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String description;
    private LocalDate publishDate;
    private BigDecimal price;
    private BookStatus status;

    public BookResponseDto() {
    }

    public BookResponseDto(Long id, String title, String author, String description,
                           LocalDate publishDate, BigDecimal price, BookStatus status) {
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}

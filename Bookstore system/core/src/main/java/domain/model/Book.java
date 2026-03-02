package domain.model;

import domain.model.impl.Identifiable;
import enums.BookStatus;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonAutoDetect
public class Book implements Identifiable {
    private Long id;
    private String title;
    private String author;
    private String description;
    @JsonFormat (pattern = "yyyy-MM-dd", shape =  JsonFormat.Shape.STRING)
    private LocalDate publishDate;
    private BigDecimal price;
    private BookStatus status;

    public Book() {}
    public Book (String title, String author, String description, LocalDate publishDate, BigDecimal price, BookStatus status){
        this.title = title;
        this.author = author;
        this.description = description;
        this.publishDate = publishDate;
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

    public BigDecimal getPrice() {
        return price;
    }

    public BookStatus getStatus() {
        return status;
    }

    @JsonIgnore
    public boolean isAvailable(){
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

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

}

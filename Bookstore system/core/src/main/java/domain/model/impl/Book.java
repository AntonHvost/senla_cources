package domain.model.impl;

import domain.model.Identifiable;
import enums.BookStatus;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonAutoDetect

@Entity
@Table(name = "book", schema = "public")
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
    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    @Column(name = "author")
    public String getAuthor() {
        return author;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @Column(name = "publish_date")
    public LocalDate getPublishDate() {
        return publishDate;
    }

    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    @Column(name = "status")
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

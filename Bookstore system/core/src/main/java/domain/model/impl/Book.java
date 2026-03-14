package domain.model.impl;

import domain.model.Identifiable;
import enums.BookStatus;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "book", schema = "public")
public class Book implements Identifiable {
    private Long id;
    private String title;
    private String author;
    private String description;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "publish_date")
    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)",name = "status")
    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    @Transient
    public boolean isAvailable(){
        return status == BookStatus.AVAILABLE;
    }

}

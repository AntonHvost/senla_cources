package t_4.model;

import t_4.enums.BookStatus;

import java.math.BigDecimal;

public class Book {
    private long id;
    private String title;
    private String author;
    private BookStatus status;

    public Book (long id, String title, String author, BookStatus status){
        this.id = id;
        this.title = title;
        this.author = author;
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

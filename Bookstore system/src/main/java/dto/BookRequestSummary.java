package dto;

import domain.model.Book;
import domain.model.BookRequest;

import java.util.List;

public class BookRequestSummary {
    private Book book;
    private long requestCount;
    private List<BookRequest> requests;

    public BookRequestSummary (Book book, List<BookRequest> requests) {
        this.book = book;
        this.requests = requests;
        this.requestCount = requests.size();
    }

    public Book getBook() {
        return book;
    }

    public List<BookRequest> getRequests() {
        return requests;
    }

    public long getRequestCount() {
        return requestCount;
    }
}

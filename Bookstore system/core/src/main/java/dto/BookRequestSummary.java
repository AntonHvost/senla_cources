package dto;

import domain.model.impl.Book;
import domain.model.impl.BookRequest;
import dto.response.BookRequestResponseDto;
import dto.response.BookResponseDto;

import java.util.List;

public class BookRequestSummary {
    private BookResponseDto book;
    private long requestCount;
    private List<BookRequestResponseDto> requests;

    public BookRequestSummary (BookResponseDto book, List<BookRequestResponseDto> requests) {
        this.book = book;
        this.requests = requests;
        this.requestCount = requests.size();
    }

    public BookResponseDto getBook() {
        return book;
    }

    public List<BookRequestResponseDto> getRequests() {
        return requests;
    }

    public long getRequestCount() {
        return requestCount;
    }
}

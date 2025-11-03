package t1.model;

import t1.enums.RequestStatus;

import java.time.LocalDateTime;
import java.util.Date;

public class BookRequest {
    private long id;
    private Book reqBook;
    private LocalDateTime requestDate;
    private Order relatedOrder;
    private RequestStatus status;

    public BookRequest(long id, Book book, Order order) {
        this.id = id;
        this.reqBook = book;
        this.relatedOrder = order;
        this.requestDate = LocalDateTime.now();
        this.status = RequestStatus.PENDING;
    }

    public long getId() {
        return id;
    }

    public Book getReqBook() {
        return reqBook;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public Order getRelatedOrder() {
        return relatedOrder;
    }

    public void fulFilled(){
        status = RequestStatus.FULFILLED;
    }

    public void cancel(){
        status = RequestStatus.CANCELLED;
    }
}

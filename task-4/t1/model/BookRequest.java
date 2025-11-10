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
        this.requestDate = LocalDateTime.now();
        this.relatedOrder = order;
        this.status = RequestStatus.PENDING;
    }

    public long getId() {
        return id;
    }

    public Book getReqBook () {
        return reqBook;
    }

    public LocalDateTime getRequestDate () {
        return requestDate;
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
        this.status = RequestStatus.FULFILLED;
    }

    public void cancel(){
        status = RequestStatus.CANCELLED;
    }
}

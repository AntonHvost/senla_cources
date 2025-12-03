package bookstore_system.domain.model;

import bookstore_system.enums.RequestStatus;

import java.time.LocalDateTime;

public class BookRequest implements Indedifiable{
    private Long id;
    private Long reqBookId;
    private Long relatedOrderId;
    private LocalDateTime requestDate;
    private LocalDateTime deliveryDate;
    private RequestStatus status;

    public  BookRequest() {}

    public BookRequest(Long id, Long bookId, Long orderId) {
        this.id = id;
        this.reqBookId = bookId;
        this.relatedOrderId = orderId;
        this.requestDate = LocalDateTime.now();
        this.deliveryDate = null;
        this.status = RequestStatus.PENDING;
    }
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getReqBookId () {
        return reqBookId;
    }

    public LocalDateTime getRequestDate () {
        return requestDate;
    }

    public LocalDateTime getDeliveryDate () {
        return deliveryDate;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public Long getRelatedOrder() {
        return relatedOrderId;
    }

    public void setReqBookId(Long reqBookId) {
        this.reqBookId = reqBookId;
    }

    public void setRelatedOrderId(Long relatedOrderId) {
        this.relatedOrderId = relatedOrderId;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void fulFilled(){
        this.status = RequestStatus.FULFILLED;
    }

    public void cancel(){
        status = RequestStatus.CANCELLED;
    }
}

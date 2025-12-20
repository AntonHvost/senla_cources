package bookstore_system.domain.model;

import bookstore_system.domain.model.impl.Identifiable;
import bookstore_system.enums.RequestStatus;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@JsonAutoDetect
public class BookRequest implements Identifiable {
    private Long id;
    private Long reqBookId;
    private Long relatedOrderId;
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestDate;
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveryDate;
    private RequestStatus status;

    public  BookRequest() {}

    public BookRequest(Long bookId, Long orderId) {
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

    public Long getRelatedOrderId() {
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

package domain.model.impl;

import domain.model.Identifiable;
import enums.RequestStatus;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@JsonAutoDetect
@Entity
@Table(name = "book_request")
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
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_request_seq")
    @SequenceGenerator(name = "book_request_seq",
            sequenceName = "book_request_id_seq",
            allocationSize = 1)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "book_id")
    public Long getReqBookId () {
        return reqBookId;
    }

    @Column(name = "order_id")
    public Long getRelatedOrderId() {
        return relatedOrderId;
    }

    @Column(name = "create_at")
    public LocalDateTime getRequestDate () {
        return requestDate;
    }

    @Column(name = "delivery_date")
    public LocalDateTime getDeliveryDate () {
        return deliveryDate;
    }

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)",name = "status")
    public RequestStatus getStatus() {
        return status;
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

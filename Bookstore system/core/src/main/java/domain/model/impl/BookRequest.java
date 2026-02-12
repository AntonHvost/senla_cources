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
    private Book reqBook;
    private Order relatedOrder;
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestDate;
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveryDate;
    private RequestStatus status;

    public  BookRequest() {}

    public BookRequest(Book book, Order order) {
        this.reqBook = book;
        this.relatedOrder = order;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    public Book getReqBook () {
        return reqBook;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "order_id")
    public Order getRelatedOrder() {
        return relatedOrder;
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

    public void setReqBook(Book reqBook) {
        this.reqBook = reqBook;
    }

    public void setRelatedOrder(Order relatedOrder) {
        this.relatedOrder = relatedOrder;
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

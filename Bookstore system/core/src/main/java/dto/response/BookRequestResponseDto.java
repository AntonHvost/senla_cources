package dto.response;

import enums.RequestStatus;

import java.time.LocalDateTime;

public class BookRequestResponseDto {
    Long orderId;
    LocalDateTime requestDate;
    LocalDateTime deliveryDate;
    RequestStatus requestStatus;

    public BookRequestResponseDto() {
    }

    public BookRequestResponseDto(Long orderId, LocalDateTime requestDate, LocalDateTime deliveryDate, RequestStatus requestStatus) {
        this.orderId = orderId;
        this.requestDate = requestDate;
        this.deliveryDate = deliveryDate;
        this.requestStatus = requestStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}

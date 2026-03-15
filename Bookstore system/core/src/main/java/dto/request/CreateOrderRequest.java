package dto.request;

import java.util.List;

public class CreateOrderRequest {
    private ConsumerRequestDto consumer;
    private List<OrderItemRequest> items;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(ConsumerRequestDto consumer, List<OrderItemRequest> items) {
        this.consumer = consumer;
        this.items = items;
    }

    public ConsumerRequestDto getConsumer() {
        return consumer;
    }

    public void setConsumerId(Long consumerId) {
        this.consumer = consumer;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}

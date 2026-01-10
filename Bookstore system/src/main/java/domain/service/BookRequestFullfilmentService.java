package domain.service;

import config.ConfigProperty;
import di.annotation.Component;
import di.annotation.Inject;
import domain.model.BookRequest;
import enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookRequestFullfilmentService {
    private final RequestService requestService;
    private final OrderService orderService;

    @ConfigProperty(propertyName = "autoCompleteRequest",  type = boolean.class)
    private boolean isAutoCompleteRequest;

    @Inject
    public BookRequestFullfilmentService(RequestService requestService, OrderService orderService) {
        this.requestService = requestService;
        this.orderService = orderService;
    }
    public void fulfillRequests(long bookId){

        List<BookRequest> bookRequests = requestService.findPendingRequestsByBookId(bookId);

        for(BookRequest bookRequest : bookRequests){
            if (isAutoCompleteRequest) bookRequest.fulFilled();

            bookRequest.setDeliveryDate(LocalDateTime.now());

            Long orderId = bookRequest.getRelatedOrderId();
            if(orderService.findOrderById(orderId).isPresent()){
                orderService.updateOrderStatus(orderId, OrderStatus.IN_PROCESS);
            } else {
                System.out.println("Заказ №" + orderId + " был закрыт, либо отсутствует!");
            }

        }
    }
}

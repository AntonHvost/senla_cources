package bookstore_system.domain.service;

import bookstore_system.config.BookstoreConfig;
import bookstore_system.config.ConfigProperty;
import bookstore_system.di.annotation.Component;
import bookstore_system.di.annotation.Inject;
import bookstore_system.domain.model.BookRequest;
import bookstore_system.enums.OrderStatus;

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

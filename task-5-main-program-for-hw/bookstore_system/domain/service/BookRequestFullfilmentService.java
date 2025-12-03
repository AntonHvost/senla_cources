package bookstore_system.domain.service;

import bookstore_system.domain.model.BookRequest;
import bookstore_system.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class BookRequestFullfilmentService {
    private final RequestService requestService;
    private final OrderService orderService;

    public BookRequestFullfilmentService(RequestService requestService, OrderService orderService) {
        this.requestService = requestService;
        this.orderService = orderService;
    }
    public void fulfillRequests(long bookId){

        List<BookRequest> bookRequests = requestService.findPendingRequestsByBookId(bookId);

        for(BookRequest bookRequest : bookRequests){
            bookRequest.fulFilled();
            bookRequest.setDeliveryDate(LocalDateTime.now());

            Long orderId = bookRequest.getRelatedOrder();
            if(orderService.findOrderById(orderId).isPresent()){
                orderService.updateOrderStatus(orderId, OrderStatus.IN_PROCESS);
            } else {
                System.out.println("Заказ №" + orderId + " был закрыт, либо отсутствует!");
            }

        }
    }
}

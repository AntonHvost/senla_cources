package service;

import config.ConfigProperty;
import database.TransactionManager;
import di.annotation.Component;
import di.annotation.Inject;
import domain.model.BookRequest;
import enums.OrderStatus;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookRequestFullfilmentService {
    private final RequestService requestService;
    private final OrderService orderService;

    @ConfigProperty(propertyName = "autoCompleteRequest",  type = boolean.class)
    private boolean isAutoCompleteRequest;
    private final TransactionManager transactionManager;

    @Inject
    public BookRequestFullfilmentService(RequestService requestService, OrderService orderService, TransactionManager transactionManager) {
        this.requestService = requestService;
        this.orderService = orderService;
        this.transactionManager = transactionManager;
    }
    public void fulfillRequests(long bookId){
        transactionManager.beginTransaction();
        try {
        List<BookRequest> bookRequests = requestService.findPendingRequestsByBookId(bookId);
            for(BookRequest bookRequest : bookRequests){
                if (isAutoCompleteRequest) bookRequest.fulFilled();

                bookRequest.setDeliveryDate(LocalDateTime.now());
                Long orderId = bookRequest.getRelatedOrderId();

                requestService.update(bookRequest);

                if(orderService.findOrderById(orderId).isPresent()){
                    orderService.updateOrderStatus(orderId, OrderStatus.IN_PROCESS);
                } else {
                    System.out.println("Заказ №" + orderId + " был закрыт, либо отсутствует!");
                }
            }
            transactionManager.commitTransaction();
        } catch(Exception e){
            transactionManager.rollbackTransaction();
            e.printStackTrace();
        }

    }
}

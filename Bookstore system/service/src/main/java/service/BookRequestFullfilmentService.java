package service;

import config.ConfigProperty;
import di.annotation.Component;
import di.annotation.Inject;
import domain.model.impl.BookRequest;
import enums.OrderStatus;

import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HibernateUtil;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookRequestFullfilmentService {
    private static final Logger logger = LoggerFactory.getLogger(BookRequestFullfilmentService.class);

    private final OrderService orderService;
    private final RequestService requestService;

    @ConfigProperty(propertyName = "autoCompleteRequest",  type = boolean.class)
    private boolean isAutoCompleteRequest;

    @Inject
    public BookRequestFullfilmentService(RequestService requestService, OrderService orderService) {
        this.requestService = requestService;
        this.orderService = orderService;
    }
    public void fulfillRequests(long bookId){
        logger.info("Fulfilling pending requests for book ID: {}", bookId);
        Transaction trx = HibernateUtil.getSession().beginTransaction();
        try {
            List<BookRequest> bookRequests = requestService.findPendingRequestsByBookId(bookId);
            logger.debug("Found {} pending requests to fulfill", bookRequests.size());

            for(BookRequest bookRequest : bookRequests){
                if (isAutoCompleteRequest) {
                    bookRequest.fulFilled();
                    logger.debug("Auto-completed request ID: {}", bookRequest.getId());
                }

                bookRequest.setDeliveryDate(LocalDateTime.now());
                Long orderId = bookRequest.getRelatedOrderId();

                requestService.update(bookRequest);

                if(orderService.findOrderById(orderId).isPresent()){
                    orderService.updateOrderStatus(orderId, OrderStatus.IN_PROCESS);
                    logger.info("Order ID {} moved to IN_PROCESS due to fulfilled request", orderId);
                } else {
                    System.out.println("Заказ №" + orderId + " был закрыт, либо отсутствует!");
                    logger.warn("Order ID {} not found during request fulfillment", orderId);
                }
            }
            trx.commit();
            logger.info("Successfully fulfilled {} requests for book ID {}", bookRequests.size(), bookId);
        } catch(Exception e){
            trx.rollback();
            logger.error("Error fulfilling requests for book ID {}", bookId, e);
            throw new RuntimeException("Request fulfillment failed", e);
        }

    }
}

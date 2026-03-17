package service;

import domain.model.impl.BookRequest;
import enums.OrderStatus;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookRequestFullfilmentService {
    private static final Logger logger = LoggerFactory.getLogger(BookRequestFullfilmentService.class);

    private final OrderService orderService;
    private final RequestService requestService;

    @Value("${autoCompleteRequest}")
    private boolean isAutoCompleteRequest;

    public BookRequestFullfilmentService(RequestService requestService, OrderService orderService) {
        this.requestService = requestService;
        this.orderService = orderService;
    }

    @Transactional
    public boolean fulfillRequests(long bookId){
        logger.info("Fulfilling pending requests for book ID: {}", bookId);
        try {
            List<BookRequest> bookRequests = requestService.findPendingRequestsByBookId(bookId);
            logger.debug("Found {} pending requests to fulfill", bookRequests.size());

            for(BookRequest bookRequest : bookRequests){
                if (isAutoCompleteRequest) {
                    bookRequest.fulFilled();
                    logger.debug("Auto-completed request ID: {}", bookRequest.getId());
                }

                bookRequest.setDeliveryDate(LocalDateTime.now());
                Long orderId = bookRequest.getRelatedOrder().getId();

                requestService.update(bookRequest);

                if(orderService.findOrderById(orderId).isPresent()){
                    orderService.updateOrderStatus(orderId, OrderStatus.IN_PROCESS);
                    logger.info("Order ID {} moved to IN_PROCESS due to fulfilled request", orderId);
                } else {
                    System.out.println("Заказ №" + orderId + " был закрыт, либо отсутствует!");
                    logger.warn("Order ID {} not found during request fulfillment", orderId);
                    return false;
                }
            }
            logger.info("Successfully fulfilled {} requests for book ID {}", bookRequests.size(), bookId);
        } catch(Exception e){
            logger.error("Error fulfilling requests for book ID {}", bookId, e);
            throw new RuntimeException("Request fulfillment failed", e);
        }
        return true;
    }
}

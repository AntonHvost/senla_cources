package service;

import domain.model.impl.Book;
import enums.BookStatus;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import util.HibernateUtil;
import di.annotation.Component;
import di.annotation.Inject;
import domain.model.impl.Consumer;
import domain.model.impl.Order;
import domain.model.impl.OrderItem;
import repository.impl.OrderItemRepository;
import repository.impl.OrderRepository;
import enums.OrderStatus;
import enums.RequestStatus;

@Component
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final BookInventoryService bookInventoryService;
    private final RequestService requestService;
    private final ConsumerService consumerService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;


    @Inject
    public OrderService(RequestService requestService,
                        BookInventoryService bookInventoryService,
                        ConsumerService consumerService,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository
    ) {
        this.requestService = requestService;
        this.bookInventoryService = bookInventoryService;
        this.consumerService = consumerService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    private BigDecimal calculateTotalPrice(Order order) {
        BigDecimal totalPrice;
        totalPrice = order.getOrderItemsList().stream()
                .map(orderItem -> orderItem.getBook().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.debug("Calculated total price: {}", totalPrice);
        return totalPrice;
    }

    /*private BigDecimal calculateTotalPrice(Order order) {
        BigDecimal totalPrice;
        totalPrice = order.getOrderItemsList().stream()
                .map(orderItem ->
                        bookInventoryService.findBookById(orderItem.getBookId())
                            .map(book -> book.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                            .orElse(BigDecimal.ZERO)
                ).reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.debug("Calculated total price: {}", totalPrice);
        return totalPrice;
    }*/

    public Order createOrder(long[] bookIds, int[] quantities, Consumer consumer) {
        logger.info("Creating new order for consumer: {}", consumer.getName());
        logger.debug("Order items: {} books", bookIds.length);
        Transaction trx = HibernateUtil.getSession().beginTransaction();
        try {
            logger.debug("Transaction started for order creation");
            Order order = new Order(consumer);
            orderRepository.save(order);
            HibernateUtil.getSession().flush();

            logger.debug("Order ID {} created", order.getId());

            List<Book> books = bookInventoryService.getBooks();
            Map<Long, Book> bookMap = books.stream()
                    .collect(Collectors.toMap(Book::getId, Function.identity()));

            for(int i = 0; i < bookIds.length; i++){

                long bookId = bookIds[i];
                int quantity = quantities[i];
                Book book = bookMap.get(bookId);

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setBook(book);
                orderItem.setQuantity(quantity);

                if(!book.isAvailable()){
                    logger.warn("Book ID {} is unavailable, creating request", book.getId());
                    requestService.createRequest(book, order);
                }

                order.addItem(orderItem);
            }
            order.setTotalPrice(calculateTotalPrice(order));
            orderRepository.update(order);
            trx.commit();

            logger.info("Order ID {} created successfully with total: {}", order.getId(), order.getTotalPrice());

            return order;

        } catch (Exception e) {
            trx.rollback();
            logger.error("Failed to create order for consumer: {}", consumer.getName(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean completeOrder(Long orderId) {
        logger.info("Attempting to complete order ID: {}", orderId);
        Transaction trx = HibernateUtil.getSession().beginTransaction();
        try {
            boolean result = orderRepository.findById(orderId)
                    .filter(order -> requestService.getRequestStatusByOrderId(orderId) == RequestStatus.FULFILLED || requestService.getRequestStatusByOrderId(orderId) == null)
                    .filter(Order::canBeCompleted)
                    .map(order ->
                    {
                        order.setOrderStatus(OrderStatus.COMPLETED);
                        order.setCompletedAtDate(LocalDateTime.now());
                        orderRepository.update(order);
                        logger.info("Order ID {} completed successfully", orderId);
                        return true;
                    })
                    .orElse(false);
            trx.commit();
            return result;
        } catch (Exception e) {
            trx.rollback();
            logger.error("Failed to complete order for order ID: {}", orderId, e);
        }
        return false;
    }

    public void cancelOrder(Long orderId) {
        logger.info("Cancelling order ID: {}", orderId);
        Transaction trx = HibernateUtil.getSession().beginTransaction();
        try {
            orderRepository.findById(orderId).ifPresent(order -> {
                order.setOrderStatus(OrderStatus.CANCELLED);
                order.setCompletedAtDate(LocalDateTime.now());
                orderRepository.update(order);
                logger.info("Order ID {} cancelled", orderId);
            });
        } catch (Exception e) {
            trx.rollback();
            logger.error("Failed to cancel order for order ID: {}", orderId, e);
        }
    }

    public Optional<Order> findOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getOrderList() {
        logger.debug("Fetching all orders");
        return orderRepository.findAll();
    }

    public void updateOrderStatus(long id, OrderStatus status) {
        logger.info("Updating order ID {} status to: {}", id, status);
        Transaction trx = HibernateUtil.getSession().beginTransaction();
        try {
            orderRepository.findById(id).ifPresent(order -> {
                order.setOrderStatus(status);
                if (order.getOrderStatus() == OrderStatus.COMPLETED || order.getOrderStatus() == OrderStatus.CANCELLED) {
                    order.setCompletedAtDate(LocalDateTime.now());
                }
                orderRepository.update(order);
                logger.debug("Order ID {} status updated", id);
            });
            trx.commit();
            logger.info("Order ID {} updated successfully", id);
        } catch (Exception e) {
            trx.rollback();
            logger.error("Failed to update order status for order ID: {}", id, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public void saveOrder(Order order) {
        logger.debug("Saving order: ID={}", order.getId());
        orderRepository.save(order);
    }

    public void updateOrder(Order order){
        logger.debug("Updating order: ID={}", order.getId());
        orderRepository.update(order);
    }

}

package service;

import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public Order createOrder(long[] bookIds, int[] quantities, Consumer consumer) {
        logger.info("Creating new order for consumer: {}", consumer.getName());
        logger.debug("Order items: {} books", bookIds.length);
        Transaction trx = HibernateUtil.getSession().beginTransaction();
        try{

            logger.debug("Transaction started for order creation");

            consumerService.save(consumer);
            Order order = new Order(consumer.getId());
            orderRepository.save(order);
            logger.debug("Order ID {} created", order.getId());

            for (int i = 0; i < bookIds.length; i++) {
                long bookId = bookIds[i];
                int quantity = quantities[i];

                if(!bookInventoryService.isAvailable(bookId)){
                    logger.warn("Book ID {} is unavailable, creating request", bookId);
                    requestService.createRequest(bookId, order.getId());
                }

                OrderItem item = new OrderItem();
                item.setOrderId(order.getId());
                item.setBookId(bookId);
                item.setQuantity(quantity);

                order.addItem(item);

                orderItemRepository.save(item);
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
        return orderRepository.findById(orderId)
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.getItemByOrderId(order.getId());
                    order.setOrderItemsList(items);
                    return order;
                });
    }

    public List<Order> getOrderList() {
        logger.debug("Fetching all orders");
        return orderRepository.findAll();
    }

    public void updateOrderStatus(long id, OrderStatus status) {
        logger.info("Updating order ID {} status to: {}", id, status);
        orderRepository.findById(id).ifPresent(order -> {
            order.setOrderStatus(status);
            if (order.getOrderStatus() == OrderStatus.COMPLETED || order.getOrderStatus() == OrderStatus.CANCELLED) {
                order.setCompletedAtDate(LocalDateTime.now());
            }
            orderRepository.update(order);
            logger.debug("Order ID {} status updated", id);
        });
    }

    private BigDecimal calculateTotalPrice(Order order) {
        BigDecimal totalPrice;
        totalPrice = order.getOrderItemsList().stream()
                .map(orderItem ->
                        bookInventoryService.findBookById(orderItem.getBookId())
                            .map(book -> book.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                            .orElse(BigDecimal.ZERO)
                ).reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.debug("Calculated total price: {}", totalPrice);
        return totalPrice;
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

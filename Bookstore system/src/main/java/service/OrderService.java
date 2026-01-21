package service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.TransactionManager;
import di.annotation.Component;
import di.annotation.Inject;
import domain.model.Consumer;
import domain.model.Order;
import domain.model.OrderItem;
import repository.OrderItemRepository;
import repository.OrderRepository;
import enums.OrderStatus;
import enums.RequestStatus;

@Component
public class OrderService {
    private final BookInventoryService bookInventoryService;
    private final RequestService requestService;
    private final ConsumerService consumerService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final TransactionManager transactionManager;


    @Inject
    public OrderService(RequestService requestService,
                        BookInventoryService bookInventoryService,
                        ConsumerService consumerService,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        TransactionManager transactionManager) {
        this.requestService = requestService;
        this.bookInventoryService = bookInventoryService;
        this.consumerService = consumerService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.transactionManager = transactionManager;
        System.out.println(this.transactionManager == null);
    }

    public Order createOrder(long[] bookIds, int[] quantities, Consumer consumer) {

        try {
            transactionManager.beginTransaction();

            consumerService.save(consumer);

            Order order = new Order(consumer.getId());
            orderRepository.save(order);

            for (int i = 0; i < bookIds.length; i++) {
                long bookId = bookIds[i];
                int quantity = quantities[i];

                if(!bookInventoryService.isAvailable(bookId)){
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

            transactionManager.commitTransaction();
            return order;
        } catch (Exception e) {
            transactionManager.rollbackTransaction();
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean completeOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .filter(order -> requestService.getRequestStatusByOrderId(orderId) == RequestStatus.FULFILLED || requestService.getRequestStatusByOrderId(orderId) == null)
                .filter(Order::canBeCompleted)
                .map(order ->
                {
                    order.setOrderStatus(OrderStatus.COMPLETED);
                    order.setCompletedAtDate(LocalDateTime.now());
                    orderRepository.update(order);
                    return true;
                })
                .orElse(false);
    }

    public void cancelOrder(Long orderId) {
        orderRepository.findById(orderId).ifPresent(order->{
            order.setOrderStatus(OrderStatus.CANCELLED);
            order.setCompletedAtDate(LocalDateTime.now());
            orderRepository.update(order);
        });
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
        return orderRepository.findAll();
    }

    public void updateOrderStatus(long id, OrderStatus status) {
       orderRepository.findById(id).ifPresent(order -> {
            order.setOrderStatus(status);
            if (order.getOrderStatus() == OrderStatus.COMPLETED || order.getOrderStatus() == OrderStatus.CANCELLED) {
                order.setCompletedAtDate(LocalDateTime.now());
            }
            orderRepository.update(order);
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
        return totalPrice;
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public void updateOrder(Order order){
        orderRepository.update(order);
    }

}

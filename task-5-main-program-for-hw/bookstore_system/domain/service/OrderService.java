package bookstore_system.domain.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import bookstore_system.di.annotation.Component;
import bookstore_system.di.annotation.Inject;
import bookstore_system.domain.model.Book;
import bookstore_system.domain.model.Consumer;
import bookstore_system.domain.model.Order;
import bookstore_system.domain.model.OrderItem;
import bookstore_system.domain.repository.OrderRepository;
import bookstore_system.enums.BookStatus;
import bookstore_system.enums.OrderStatus;
import bookstore_system.enums.RequestStatus;

@Component
public class OrderService {
    private final BookInventoryService bookInventoryService;
    private final RequestService requestService;
    private final ConsumerService consumerService;
    private final OrderRepository orderRepository;

    @Inject
    public OrderService(RequestService requestService, BookInventoryService bookInventoryService, ConsumerService consumerService, OrderRepository orderRepository) {
        this.requestService = requestService;
        this.bookInventoryService = bookInventoryService;
        this.consumerService = consumerService;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(long[] bookIds, int[] quantities, Consumer consumer) {
        consumerService.save(consumer);
        Order order = new Order(consumer.getId());
        order.setId(orderRepository.generateNextId());

        for (int i = 0; i < bookIds.length; i++) {
            if(bookInventoryService.isAvailable(bookIds[i])){
                requestService.createRequest(bookIds[i], order.getId());
            }
            order.addItem(new OrderItem(orderRepository.generateNextItemId(), order.getId(), bookIds[i], quantities[i]));
        }

        order.setTotalPrice(calculateTotalPrice(order));
        order.setOrderStatus(OrderStatus.NEW);

        orderRepository.save(order);

        return order;
    }

    public boolean completeOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .filter(order -> requestService.getRequestStatusByOrderId(orderId) == RequestStatus.FULFILLED || requestService.getRequestStatusByOrderId(orderId) == null)
                .filter(Order::canBeCompleted)
                .map(order ->
                {
                    order.setOrderStatus(OrderStatus.COMPLETED);
                    order.setCompletedAtDate(LocalDateTime.now());
                    return true;
                })
                .orElse(false);
    }

    public void cancelOrder(Long orderId) {
        orderRepository.findById(orderId).ifPresent(order->{
            order.setOrderStatus(OrderStatus.CANCELLED);
            order.setCompletedAtDate(LocalDateTime.now());
        });
    }

    public Optional<Order> findOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getOrderList() {
        return orderRepository.findAll();
    }

    /*public Long getNextOrderId() {
        return nextOrderId;
    }*/

    public void updateOrderStatus(long id, OrderStatus status) {
       orderRepository.findById(id).ifPresent(order -> {
            order.setOrderStatus(status);
            if (order.getOrderStatus() == OrderStatus.COMPLETED || order.getOrderStatus() == OrderStatus.CANCELLED) {
                order.setCompletedAtDate(LocalDateTime.now());
            }
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
        if(order.getId() == null || order.getId() == 0){
            order.setId(orderRepository.generateNextId());
        }
        orderRepository.save(order);
    }

    public void updateOrder(Order order){
        orderRepository.update(order);
    }

}

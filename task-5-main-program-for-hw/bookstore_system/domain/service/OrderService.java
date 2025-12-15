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
import bookstore_system.enums.BookStatus;
import bookstore_system.enums.OrderStatus;
import bookstore_system.enums.RequestStatus;

@Component
public class OrderService {
    private final BookInventoryService bookInventoryService;
    private final RequestService requestService;
    private final ConsumerService consumerService;
    private List<Order> ordersList = new ArrayList<>();
    private Long nextOrderId = 1L;
    private Long nextOrderItemId = 1L;

    @Inject
    public OrderService(RequestService requestService, BookInventoryService bookInventoryService, ConsumerService consumerService) {
        this.requestService = requestService;
        this.bookInventoryService = bookInventoryService;
        this.consumerService = consumerService;
    }

    public Order createOrder(long[] bookIds, int[] quantities, Consumer consumer) {
        consumerService.save(consumer);
        System.out.println(consumer.getName());
        Order order = new Order(consumer.getId());

        for (int i = 0; i < bookIds.length; i++) {
            Book book = bookInventoryService.findBookById(bookIds[i])
                    .orElseThrow(() -> new IllegalArgumentException("Ошибка создания заказа! Книга не найдена в каталоге!"));

            if (book.getStatus() != BookStatus.AVAILABLE) {
                requestService.createRequest(book.getId(), order.getId());
            }
            order.addItem(new OrderItem(nextOrderItemId++, order.getId(), book.getId(), quantities[i]));
        }

        order.setTotalPrice(calculateTotalPrice(order));
        ordersList.add(order);
        order.setId(nextOrderId++);
        order.setOrderStatus(OrderStatus.NEW);
        return order;
    }

    public boolean completeOrder(Long orderId) {
        return findOrderById(orderId)
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
        findOrderById(orderId).ifPresent(order->{
            order.setOrderStatus(OrderStatus.CANCELLED);
            order.setCompletedAtDate(LocalDateTime.now());
        });
    }

    public Optional<Order> findOrderById(Long orderId) {
        return ordersList.stream().filter(o -> o.getId().equals(orderId)).findAny();
    }

    public List<Order> getOrderList() {
        return ordersList;
    }

    public Long getNextOrderId() {
        return nextOrderId;
    }

    public Long getNextOrderItemId() {
        return nextOrderItemId;
    }

    public void updateOrderStatus(long id, OrderStatus status) {
        findOrderById(id).ifPresent(order -> {
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
        ordersList.add(order);
    }

    public void updateOrder(Order order){
        for (Order o : ordersList){
            if (o.getId().equals(order.getId())) {
                ordersList.set(ordersList.indexOf(o), order);
                return;
            }
        }
    }

}

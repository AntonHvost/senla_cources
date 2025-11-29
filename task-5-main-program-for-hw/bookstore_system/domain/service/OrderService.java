package bookstore_system.domain.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import bookstore_system.domain.model.Book;
import bookstore_system.domain.model.Consumer;
import bookstore_system.domain.model.Order;
import bookstore_system.domain.model.OrderItem;
import bookstore_system.enums.BookStatus;
import bookstore_system.enums.OrderStatus;
import bookstore_system.enums.RequestStatus;

public class OrderService {
    private final BookInventoryService catalog;
    private final RequestService requestService;
    private final List<Order> ordersList;
    private long nextOrderId = 1;

    public OrderService(RequestService requestService, BookInventoryService catalog){
        this.requestService = requestService;
        this.ordersList = new ArrayList<>();
        this.catalog = catalog;
    }

    public Order createOrder(long[] bookIds, int[] quantities, Long consumerId) {
        Order order = new Order(nextOrderId++, consumerId);

        for (int i = 0; i < bookIds.length; i++) {
            Book book = catalog.findBookById(bookIds[i])
                    .orElseThrow(() -> new IllegalArgumentException("Ошибка создания заказа! Книга не найдена в каталоге!"));

            if (book.getStatus() != BookStatus.AVAILABLE) {
                requestService.createRequest(book, order);
            }
            order.addItem(new OrderItem(i + 1, book, quantities[i]));
        }

        order.calculateTotalPrice();
        ordersList.add(order);
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
        return ordersList.stream().filter(o -> o.getId() == orderId).findAny();
    }

    public List<Order> getOrderList() {
        return ordersList;
    }

    public void updateOrderStatus(long id, OrderStatus status) {
        findOrderById(id).ifPresent(order -> {
            order.setOrderStatus(status);
            if (order.getOrderStatus() == OrderStatus.COMPLETED || order.getOrderStatus() == OrderStatus.CANCELLED) {
                order.setCompletedAtDate(LocalDateTime.now());
            }
        });
    }

}

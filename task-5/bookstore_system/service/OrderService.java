package bookstore_system.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import bookstore_system.enums.BookStatus;
import bookstore_system.enums.OrderStatus;
import bookstore_system.enums.RequestStatus;

import bookstore_system.domain.*;

public class OrderService {
    private BookCatalog catalog;
    private RequestService requestService;
    private List<Order> ordersList;
    private long nextOrderId = 1;

    public OrderService(RequestService requestService, BookCatalog catalog){
        this.requestService = requestService;
        this.ordersList = new ArrayList<>();
        this.catalog = catalog;
    }

    public Order createOrder(long[] bookIds, int[] quantities, Consumer consumer) {
        Order order = new Order(nextOrderId++, consumer);

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
                .filter(order -> requestService.getRequestStatusByOrderId(orderId) == RequestStatus.FULFILLED)
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

package t1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import t1.enums.BookStatus;
import t1.enums.OrderStatus;
import t1.enums.RequestStatus;

import t1.model.*;

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

    public Order createOrder(long[] bookIds, Consumer consumer) {
        Order order = new Order(nextOrderId++, consumer);

        for (int i = 0; i < bookIds.length; i++) {
            Book book = catalog.findBookById(bookIds[i])
                    .orElseThrow(() -> new IllegalArgumentException("Ошибка создания заказа! Книга не найдена в каталоге!"));

            if (book.getStatus() == BookStatus.AVAILABLE) {
                order.addItem(new OrderItem(i + 1, book));
            } else {
                requestService.createRequest(book, order);
            }
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
                    return true;
                })
                .orElse(false);
    }

    public void cancelOrder(Long orderId) {
        findOrderById(orderId).ifPresent(order->{order.setOrderStatus(OrderStatus.CANCELLED);});
    }
    public void updateOrderStatus(long id, OrderStatus status) {
        findOrderById(id).ifPresent(order -> {order.setOrderStatus(status);});
    }

    public Optional<Order> findOrderById(Long orderId) {
        return ordersList.stream().filter(o -> o.getId() == orderId).findAny();
    }

    public List<Order> getOrderList() {
        return ordersList;
    }
}

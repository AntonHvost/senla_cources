package t_4.service;

import t_4.enums.BookStatus;
import t_4.enums.RequestStatus;
import t_4.model.Book;
import t_4.model.BookCatalog;
import t_4.model.Order;
import t_4.model.OrderItem;
import t_4.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public Order createOrder(long[] bookIds) {
        Order order = new Order(nextOrderId++);

        for (int i = 0; i < bookIds.length; i++) {
            Book book = catalog.findBookById(bookIds[i]);
            if (book != null) {
                if (book.getStatus() == BookStatus.AVAILABLE) {
                    order.addItem(new OrderItem(i + 1, book));
                } else {
                    requestService.createRequest(book, order);
                }
            }
            else return null;
        }

        ordersList.add(order);
        order.setOrderStatus(OrderStatus.NEW);
        return order;
    }

    public boolean completeOrder(Long orderId) {
        Order order = getOrderById(orderId);
        if (order != null && requestService.getStatusRequest(orderId) == RequestStatus.FULFILLED && order.canBeCompleted()) {
            order.setOrderStatus(OrderStatus.COMPLETED);
            return true;
        }
        else {
            return false;
        }
    }

    public void cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);
        if(order != null && !isOrderCompleted(order)){
            order.setOrderStatus(OrderStatus.CANCELLED);
        }
    }
    public void updateOrderStatus(long id, OrderStatus status) {
        Order order = getOrderById(id);
        if (order != null) order.setOrderStatus(status);
    }

    public Order getOrderById(Long orderId){
        return ordersList.stream().filter(o -> o.getId() == orderId).findFirst().orElse(null);
    }

    private boolean isOrderCompleted(Order order) {
        return order.getOrderStatus() == OrderStatus.COMPLETED;
    }
}

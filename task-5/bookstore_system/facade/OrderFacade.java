package bookstore_system.facade;

import bookstore_system.domain.Consumer;
import bookstore_system.domain.Order;
import bookstore_system.enums.OrderStatus;
import bookstore_system.service.OrderService;

public class OrderFacade {
    private final OrderService orderService;

    public OrderFacade(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order createOrder(long[] bookIds, int[] quantities, Consumer consumer) {
        return orderService.createOrder(bookIds, quantities, consumer);
    }

    public void cancelOrder(long orderId) {
        orderService.cancelOrder(orderId);
    }

    public boolean completeOrder(long orderId) {
        return orderService.completeOrder(orderId);
    }

    public void updStatusOrder(long orderId, OrderStatus status) {
        orderService.updateOrderStatus(orderId, status);
    }


}

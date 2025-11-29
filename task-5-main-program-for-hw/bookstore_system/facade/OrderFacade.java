package bookstore_system.facade;

import bookstore_system.domain.model.Consumer;
import bookstore_system.domain.model.Order;
import bookstore_system.enums.OrderStatus;
import bookstore_system.domain.service.OrderService;

public class OrderFacade {
    private final OrderService orderService;

    public OrderFacade(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order createOrder(long[] bookIds, int[] quantities, Long consumerId) {
        return orderService.createOrder(bookIds, quantities, consumerId);
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

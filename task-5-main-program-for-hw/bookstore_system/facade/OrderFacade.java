package bookstore_system.facade;

import bookstore_system.domain.model.Consumer;
import bookstore_system.domain.model.Order;
import bookstore_system.domain.service.IOService;
import bookstore_system.enums.OrderStatus;
import bookstore_system.domain.service.OrderService;
import bookstore_system.io.csv.converter.OrderCsvConverter;

public class OrderFacade {
    private final OrderService orderService;
    private final IOService ioService;
    private final OrderCsvConverter orderCsvConverter = new OrderCsvConverter();

    public OrderFacade(OrderService orderService,  IOService ioService) {
        this.orderService = orderService;
        this.ioService = ioService;
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

    public void importOrderFromCsv(String fileName) {
        ioService.importEntities(fileName, orderService::findOrderById, orderService::saveOrder, orderService::updateOrder, orderCsvConverter);
    }

    public void exportOrderToCsv(String fileName) {
        ioService.exportEntities(fileName, orderService::getOrderList, orderCsvConverter);
    }


}

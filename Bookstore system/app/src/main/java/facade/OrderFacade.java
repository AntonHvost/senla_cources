package facade;

import di.annotation.Component;
import di.annotation.Inject;
import domain.model.impl.Consumer;
import domain.model.impl.Order;
import service.IOService;
import enums.OrderStatus;
import service.OrderService;
import io.csv.converter.OrderCsvConverter;
import io.csv.converter.OrderItemCsvConverter;

@Component
public class OrderFacade {
    private final OrderService orderService;
    private final IOService ioService;
    private final OrderCsvConverter orderCsvConverter = new OrderCsvConverter();
    private final OrderItemCsvConverter orderItemCsvConverter = new OrderItemCsvConverter();

    @Inject
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

    public void exportOrderToCsv(String orderFilename, String itemFilename) {
        ioService.exportOrderWithItems(orderFilename, itemFilename, orderService::getOrderList, orderCsvConverter, orderItemCsvConverter);
    }

    public void importOrderFromCsv(String orderFilename, String itemFilename) {
        ioService.importOrderWithItems(orderFilename, itemFilename, orderService::findOrderById, orderService::saveOrder, orderService::updateOrder, orderCsvConverter, orderItemCsvConverter);
    }


}

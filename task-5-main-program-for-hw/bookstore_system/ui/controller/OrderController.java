package bookstore_system.ui.controller;

import bookstore_system.di.annotation.Component;
import bookstore_system.di.annotation.Inject;
import bookstore_system.domain.model.Consumer;
import bookstore_system.domain.model.Order;
import bookstore_system.dto.OrderSummary;
import bookstore_system.enums.OrderStatus;
import bookstore_system.enums.SortByOrder;
import bookstore_system.facade.OrderFacade;
import bookstore_system.facade.ReportFacade;

import java.util.List;
import java.util.Optional;

@Component
public class OrderController {

    private final OrderFacade orderFacade;
    private final ReportFacade reportFacade;

    @Inject
    public OrderController(OrderFacade orderFacade, ReportFacade reportFacade) {
        this.orderFacade = orderFacade;
        this.reportFacade = reportFacade;
    }

    public Order createOrder(long[] bookIds, int[] quantities, Consumer consumer) {
        return  orderFacade.createOrder(bookIds, quantities, consumer);
    }

    public void cancelOrder(Long orderId) {
        orderFacade.cancelOrder(orderId);
    }

    public void changeOrderStatus(OrderStatus orderStatus, Long orderId) {
        orderFacade.updStatusOrder(orderId, OrderStatus.NEW);
    }

    public boolean completeOrder(Long orderId) {
        return orderFacade.completeOrder(orderId);
    }

    public List<OrderSummary> getSortedOrders(SortByOrder sortByOrder) {
        return reportFacade.getOrderList(sortByOrder);
    }

    public Optional<OrderSummary> getOrder(Long orderId) {
        return reportFacade.getOrderDetails(orderId);
    }


    public void exportOrder(String orderFilename, String itemFilename) {
        orderFacade.exportOrderToCsv(orderFilename, itemFilename);
    }

    public void importOrderFromCsv(String orderFilename, String itemFilename) {
        orderFacade.importOrderFromCsv(orderFilename, itemFilename);
    }

}

package ui.controller;

import domain.model.impl.Consumer;
import domain.model.impl.Order;
import dto.OrderSummary;
import enums.OrderStatus;
import enums.SortByOrder;
import facade.OrderFacade;
import facade.ReportFacade;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    private final OrderFacade orderFacade;
    private final ReportFacade reportFacade;

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

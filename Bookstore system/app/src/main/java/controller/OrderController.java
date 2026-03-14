package controller;

import domain.model.impl.Consumer;
import domain.model.impl.Order;
import dto.OrderSummary;
import dto.request.CreateOrderRequest;
import dto.response.OrderResponseDto;
import enums.OrderStatus;
import enums.SortByOrder;
import facade.OrderFacade;
import facade.ReportFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {

    private final OrderFacade orderFacade;
    private final ReportFacade reportFacade;

    public OrderController(OrderFacade orderFacade, ReportFacade reportFacade) {
        this.orderFacade = orderFacade;
        this.reportFacade = reportFacade;
    }

    @PostMapping("/new")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody CreateOrderRequest request) {
        OrderResponseDto resp = orderFacade.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/cancel/{id}")
    public void cancelOrder(@PathVariable("id") Long orderId) {
        orderFacade.cancelOrder(orderId);
        ResponseEntity.status(HttpStatus.ACCEPTED).body(orderId);
    }

    public void changeOrderStatus(OrderStatus orderStatus, Long orderId) {
        orderFacade.updStatusOrder(orderId, OrderStatus.NEW);
    }

    public boolean completeOrder(Long orderId) {
        return orderFacade.completeOrder(orderId);
    }

    @GetMapping
    public ResponseEntity<List<OrderSummary>> getSortedOrders(@RequestParam(value = "sortBy", required = false) SortByOrder sortByOrder) {
        return ResponseEntity.ok(reportFacade.getOrderList(sortByOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderSummary> getOrder(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(reportFacade.getOrderDetails(orderId).get());
    }


    public void exportOrder(String orderFilename, String itemFilename) {
        orderFacade.exportOrderToCsv(orderFilename, itemFilename);
    }

    public void importOrderFromCsv(String orderFilename, String itemFilename) {
        orderFacade.importOrderFromCsv(orderFilename, itemFilename);
    }

}

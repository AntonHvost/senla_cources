package controller;

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
@RequestMapping("api/orders")
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

    @PostMapping("/{id}/cancel")
    public void cancelOrder(@PathVariable("id") Long orderId) {
        orderFacade.cancelOrder(orderId);
        ResponseEntity.status(HttpStatus.ACCEPTED).body(orderId);
    }

    @PostMapping("/{id}/change-status")
    public void changeOrderStatus(@RequestParam(value = "sortBy", required = false) OrderStatus orderStatus, @PathVariable("id") Long orderId) {
        orderFacade.updStatusOrder(orderId, orderStatus);
        ResponseEntity.status(HttpStatus.ACCEPTED).body(orderId);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Boolean> completeOrder(@PathVariable("id") Long orderId) {
        Boolean result = orderFacade.completeOrder(orderId);
        if(result) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getSortedOrders(@RequestParam(value = "sortBy", required = false) SortByOrder sortByOrder) {
        return ResponseEntity.ok(reportFacade.getOrderList(sortByOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(reportFacade.getOrderDetails(orderId).get());
    }


    public void exportOrder(String orderFilename, String itemFilename) {
        orderFacade.exportOrderToCsv(orderFilename, itemFilename);
    }

    public void importOrderFromCsv(String orderFilename, String itemFilename) {
        orderFacade.importOrderFromCsv(orderFilename, itemFilename);
    }

}

package controller;

import dto.OrderSummary;
import enums.SortByOrder;

import facade.ReportFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/info")
public class ReportController {

    private final ReportFacade reportFacade;

    public ReportController(ReportFacade reportFacade) {
        this.reportFacade = reportFacade;
    }

    @GetMapping
    public ResponseEntity<List<OrderSummary>> getCompletedOrderAtPeriod(@RequestParam String startDate, @RequestParam String endDate, @RequestParam(required = false) SortByOrder sortByOrder) {
        return ResponseEntity.ok(reportFacade.getCompletedOrdersAtPeriod(startDate, endDate, sortByOrder));
    }

    @GetMapping
    public ResponseEntity<Integer> getCountCompletedOrdersAtPeriod(@RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(reportFacade.getCountCompletedOrdersAtPeriod(startDate, endDate));
    }

    @GetMapping("api/profit")
    public ResponseEntity<BigDecimal> getProfitAtPeriod(@RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(reportFacade.getProfitAtPeriod(startDate, endDate));
    }
}

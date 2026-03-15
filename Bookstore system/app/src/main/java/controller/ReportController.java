package controller;

import dto.response.OrderResponseDto;
import enums.SortByOrder;

import facade.ReportFacade;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/complete-orders")
    public ResponseEntity<List<OrderResponseDto>> getCompletedOrderAtPeriod(@RequestParam(value = "sDate") String startDate, @RequestParam(value = "eDate") String endDate, @RequestParam(required = false) SortByOrder sortByOrder) {
        return ResponseEntity.ok(reportFacade.getCompletedOrdersAtPeriod(startDate, endDate, sortByOrder));
    }

    @GetMapping("/count-complete-orders")
    public ResponseEntity<Integer> getCountCompletedOrdersAtPeriod(@RequestParam(value = "sDate") String startDate, @RequestParam(value = "eDate") String endDate) {
        return ResponseEntity.ok(reportFacade.getCountCompletedOrdersAtPeriod(startDate, endDate));
    }

    @GetMapping("/profit")
    public ResponseEntity<BigDecimal> getProfitAtPeriod(@RequestParam(value = "eDate") String startDate, @RequestParam(value = "eDate") String endDate) {
        return ResponseEntity.ok(reportFacade.getProfitAtPeriod(startDate, endDate));
    }
}

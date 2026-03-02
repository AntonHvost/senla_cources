package ui.controller;

import dto.OrderSummary;
import enums.SortByOrder;

import facade.ReportFacade;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ReportController {

    private final ReportFacade reportFacade;

    public ReportController(ReportFacade reportFacade) {
        this.reportFacade = reportFacade;
    }

    public List<OrderSummary> getCompletedOrderAtPeriod(String startDate, String endDate, SortByOrder sortByOrder) {
        return reportFacade.getCompletedOrdersAtPeriod(startDate, endDate, sortByOrder);
    }

    public int getCountCompletedOrdersAtPeriod(String startDate, String endDate) {
        return reportFacade.getCountCompletedOrdersAtPeriod(startDate, endDate);
    }

    public BigDecimal getProfitAtPeriod(String startDate, String endDate) {
        return reportFacade.getProfitAtPeriod(startDate, endDate);
    }
}

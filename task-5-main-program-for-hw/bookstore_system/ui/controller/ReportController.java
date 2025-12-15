package bookstore_system.ui.controller;

import bookstore_system.di.annotation.Component;
import bookstore_system.di.annotation.Inject;
import bookstore_system.dto.OrderSummary;
import bookstore_system.enums.SortByOrder;

import bookstore_system.facade.ReportFacade;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ReportController {

    private final ReportFacade reportFacade;

    @Inject
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

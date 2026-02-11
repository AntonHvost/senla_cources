package facade;

import di.annotation.Component;
import di.annotation.Inject;
import dto.BookRequestSummary;
import dto.BookSummary;
import dto.OrderSummary;
import enums.SortByBook;
import enums.SortByOrder;
import enums.SortByRequestBook;
import enums.SortByUnsoldBook;
import service.ReportService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class ReportFacade {
    private final ReportService reportService;

    @Inject
    public ReportFacade(ReportService reportService) {
        this.reportService = reportService;
    }

    public List<OrderSummary> getCompletedOrdersAtPeriod(String startDate, String endDate, SortByOrder sortParam) {
        return reportService.getCompletedOrdersToPeriod(startDate, endDate, sortParam);
    }

    public int getCountCompletedOrdersAtPeriod(String startDate, String endDate) {
        return reportService.getCompletedOrdersCount(startDate, endDate);
    }

    public String getBookDescription(Long bookId) {
        return reportService.getDescriptionBook(bookId);
    }

    public List<BookSummary> getBookCatalog(SortByBook sortParam){
        return reportService.getBookCatalog(sortParam);
    }

    public Optional<OrderSummary> getOrderDetails(Long orderId) {
        return reportService.getOrderDetails(orderId);
    }

    public List<OrderSummary> getOrderList(SortByOrder sortParam) {
        return reportService.getOrderList(sortParam);
    }

    public BigDecimal getProfitAtPeriod(String startDate, String endDate) {
        return reportService.getProfitToPeriod(startDate, endDate);
    }

    public List<BookRequestSummary> getRequestList(SortByRequestBook sortParam) {
        return reportService.getBookRequestList(sortParam);
    }

    public List<BookSummary> getUnsoldBooks(SortByUnsoldBook sortParam) {
        return reportService.getUnsoldBooksMoreThanNMonth(sortParam);
    }

}

package bookstore_system.facade;

import bookstore_system.dto.BookRequestSummary;
import bookstore_system.dto.BookSummary;
import bookstore_system.dto.OrderSummary;
import bookstore_system.enums.*;

import bookstore_system.domain.*;

import bookstore_system.service.OrderService;
import bookstore_system.service.ReportService;
import bookstore_system.service.RequestService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class BookstoreFacade {
    private OrderService orderService;
    private RequestService requestService;
    private ReportService reportService;
    private BookCatalog catalog;

    public BookstoreFacade(OrderService orderService, RequestService requestService, ReportService reportService, BookCatalog catalog) {
        this.orderService = orderService;
        this.requestService = requestService;
        this.reportService = reportService;
        this.catalog = catalog;
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

    public void addBookToCatalog(Book book) {
        catalog.addBookToCatalog(book);
    }

    public BookRequest requestBook(long bookId) {
        Book book = catalog.findBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Книга с ID " + bookId + " не найдена"));
        return requestService.createRequest(book, null);
    }

    public BookRequest requestBookForOrder(long bookId, long orderId) {
        Book book = catalog.findBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Книга с ID " + bookId + " не найдена"));
        Order order = orderService.findOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ с ID " + orderId + "не найден"));
        return requestService.createRequest(book, order);
    }

    public boolean isBookAvailable(long bookId) {
        return catalog.findBookById(bookId)
                .map(Book::isAvaible)
                .orElse(false);
    }

    public void restockBook(long bookId){
        catalog.restockBook(bookId);
        requestService.fulfillRequests(bookId);
    }

    public List<OrderSummary> getCompletedOrdersAtPeriod(String startDate, String endDate, SortByOrder sortParam) {
        return reportService.getCompletedOrdersToPeriod(startDate, endDate, sortParam);
    }

    public int getCountCompletedOrdersAtPeriod(String startDate, String endDate) {
        return reportService.getCompletedOrdersCount(startDate, endDate);
    }

    public String getBookDescription(long bookId) {
        return reportService.getDescriptionBook(bookId);
    }

    public List<BookSummary> getBookCatalog(SortByBook sortParam){
        return reportService.getBookCatalog(sortParam);
    }

    public Optional<OrderSummary> getOrderDetails(long orderId) {
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
        return reportService.getUnsoldBooksMoreThanSixMonth(sortParam);
    }
}

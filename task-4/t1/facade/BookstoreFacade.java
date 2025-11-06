package t1.facade;

import t1.enums.OrderStatus;

import t1.enums.SortByBook;
import t1.enums.SortByOrder;
import t1.model.*;

import t1.service.OrderService;
import t1.service.ReportService;
import t1.service.RequestService;

import java.math.BigDecimal;
import java.util.List;

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

    public Order createOrder(long[] bookIds, Consumer consumer) {
        return orderService.createOrder(bookIds, consumer);
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
        requestService.fulfillRequests();
    }

    public List<Order> getCompletedOrders(String startDate, String endDate) {
        return reportService.getCompletedOrders(startDate, endDate);
    }

    public int getCountCompletedOrders(String startDate, String endDate) {
        return reportService.getCompletedOrdersCount(startDate, endDate);
    }

    public String viewBookDescription(long bookId) {
        return reportService.viewDescriptionBook(bookId);
    }

    public String getBooks(SortByBook sortParam){
        return reportService.viewBookCatalog(sortParam);
    }

    public String getOrderDetails(long orderId) {
        return reportService.getOrderDetails(orderId);
    }

    public String getOrderList(SortByOrder sortParam) {
        return reportService.viewOrderList(sortParam);
    }

    public BigDecimal getProfitToPeriod(String startDate, String endDate) {
        return reportService.getProfitToPeriod(startDate, endDate);
    }
}

package t1.facade;

import t1.enums.OrderStatus;

import t1.model.Book;
import t1.model.BookCatalog;
import t1.model.BookRequest;
import t1.model.Order;

import t1.service.OrderService;
import t1.service.RequestService;

public class BookstoreFacade {
    private OrderService orderService;
    private RequestService requestService;
    private BookCatalog catalog;

    public BookstoreFacade(OrderService orderService, RequestService requestService, BookCatalog catalog) {
        this.orderService = orderService;
        this.requestService = requestService;
        this.catalog = catalog;
    }

    public Order createOrder(long[] bookIds) {
        return orderService.createOrder(bookIds);
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
}

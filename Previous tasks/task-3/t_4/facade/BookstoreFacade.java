package t_4.facade;

import t_4.enums.OrderStatus;
import t_4.model.Book;
import t_4.model.BookCatalog;
import t_4.model.BookRequest;
import t_4.model.Order;
import t_4.service.OrderService;
import t_4.service.RequestService;

public class BookstoreFacade {
    private OrderService orderService;
    private RequestService requestService;
    private BookCatalog catalog;

    public BookstoreFacade(OrderService orderService, RequestService requestService, BookCatalog catalog) {
        this.orderService = orderService;
        this.requestService = requestService;
        this.catalog = catalog;
    }

    public Order createOrder(long[] bookIds, int[] quantities) {
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
        Book book = catalog.findBookById(bookId);
        return requestService.createRequest(book, null);
    }

    public BookRequest requestBookForOrder(long bookId, long orderId) {
        Book book = catalog.findBookById(bookId);
        Order order = orderService.getOrderById(orderId);
        return requestService.createRequest(book, order);
    }

    public boolean isBookAvailable(long bookId) {
        Book book = catalog.findBookById(bookId);
        return book != null && book.isAvaible();
    }

    public void restockBook(long bookId){
        catalog.restockBook(bookId);
        requestService.fulfillRequests();
    }
}

package t1.service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import t1.enums.OrderStatus;
import t1.enums.SortByBook;
import t1.model.BookCatalog;
import t1.model.Order;
import t1.model.Book;
import t1.service.OrderService;

public class ReportService {
    private OrderService orderService;
    private RequestService requestService;
    private BookCatalog catalog;

    public ReportService(OrderService orderService, RequestService requestService, BookCatalog catalog) {
        this.orderService = orderService;
        this.requestService = requestService;
        this.catalog = catalog;
    }

    public List<Order> getCompletedOrders(String startDate, String endDate) {
        return orderService.getOrderList().stream()
                .filter(o -> o.getOrderStatus() == OrderStatus.COMPLETED)
                .filter(o -> !o.getOrderDate().isBefore(LocalDateTime.parse(startDate)))
                .filter(o -> !o.getOrderDate().isAfter(LocalDateTime.parse(endDate)))
                .toList();
    }

    public int getCompletedOrdersCount(String startDate, String endDate) {
        return (int) orderService.getOrderList().stream()
                .filter(o -> o.getOrderStatus() == OrderStatus.COMPLETED)
                .filter(o -> !o.getOrderDate().isBefore(LocalDateTime.parse(startDate)))
                .filter(o -> !o.getOrderDate().isAfter(LocalDateTime.parse(endDate)))
                .count();
    }

    public String viewDescriptionBook(long bookId) {
        return catalog.findBookById(bookId).get().getDescription();
    }

    public List<Book> viewBookCatalog(SortByBook sortParam) {
        List<Book> books = catalog.getBooks();
        Comparator<Book> comparator = switch (sortParam) {
            case ALPHABET -> Comparator.comparing(Book::getTitle);
            case PUBLICATION_DATE -> Comparator.comparing(Book::getPublishDate);
            case PRICE -> Comparator.comparing(Book::getPrice);
            case IN_STOCK -> Comparator.comparing(Book::getStatus);
        };

        return books.stream()
                .sorted(comparator)
                .toList();
    }

}

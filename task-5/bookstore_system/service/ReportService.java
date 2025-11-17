package bookstore_system.service;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import bookstore_system.dto.*;
import bookstore_system.enums.*;

import bookstore_system.domain.*;

public class ReportService {
    private OrderService orderService;
    private RequestService requestService;
    private BookCatalog catalog;

    public ReportService(OrderService orderService, RequestService requestService, BookCatalog catalog) {
        this.orderService = orderService;
        this.requestService = requestService;
        this.catalog = catalog;
    }

    private LocalDateTime parseToDateTime(String dateStr) {
        try {
            return LocalDate.parse(dateStr).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты: " + dateStr);
        }
    }

    public List<OrderSummary> getCompletedOrdersToPeriod(String startDate, String endDate, SortByOrder sortParam) {

        LocalDateTime startDateTime = parseToDateTime(startDate);
        LocalDateTime endDateTime = parseToDateTime(endDate);

        Comparator<OrderSummary> comparator = switch (sortParam) {
            case PRICE -> Comparator.comparing(OrderSummary::getPrice);
            case COMPLETE_DATE -> Comparator.comparing(OrderSummary::getCompletedOrderDate);
            default -> throw new IllegalArgumentException("Неверный параметр сортировки: " + sortParam);
        };

        return orderService.getOrderList().stream()
                .filter(o -> o.getOrderStatus() == OrderStatus.COMPLETED)
                .filter(o -> !o.getCompletedOrderDate().isBefore(startDateTime))
                .filter(o -> !o.getCompletedOrderDate().isAfter(endDateTime))
                .map(OrderSummary::new)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public int getCompletedOrdersCount(String startDate, String endDate) {

        LocalDateTime startDateTime = parseToDateTime(startDate);
        LocalDateTime endDateTime = parseToDateTime(endDate);

        return (int) orderService.getOrderList().stream()
                .filter(o -> o.getOrderStatus() == OrderStatus.COMPLETED)
                .filter(o -> !o.getCompletedOrderDate().isBefore(startDateTime))
                .filter(o -> !o.getCompletedOrderDate().isAfter(endDateTime))
                .count();
    }

    public String getDescriptionBook(long bookId) {
        return catalog.findBookById(bookId).map(Book::getDescription).orElse("");
    }

    public List<BookSummary> getBookCatalog(SortByBook sortParam) {
        Comparator<BookSummary> comparator = switch (sortParam) {
            case ALPHABET -> Comparator.comparing(BookSummary::getTitle);
            case PUBLICATION_DATE -> Comparator.comparing(BookSummary::getPublishDate);
            case PRICE -> Comparator.comparing(BookSummary::getPrice);
            case IN_STOCK -> Comparator.comparing(BookSummary::getStatus);
        };

        return catalog.getBooks().stream()
                .map(BookSummary::new)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public List<OrderSummary> getOrderList(SortByOrder sortParam) {
        Comparator<OrderSummary> comparator = switch (sortParam) {
            case COMPLETE_DATE -> Comparator.comparing(OrderSummary::getCompletedOrderDate, Comparator.naturalOrder());
            case PRICE -> Comparator.comparing(OrderSummary::getPrice);
            case STATUS -> Comparator.comparing(OrderSummary::getStatus);
            default -> Comparator.comparing(OrderSummary::getId);
        };

        return orderService.getOrderList().stream()
                .map(OrderSummary::new)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public BigDecimal getProfitToPeriod(String startDate, String endDate) {

        LocalDateTime startDateTime = parseToDateTime(startDate);
        LocalDateTime endDateTime = parseToDateTime(endDate);

        return orderService.getOrderList().stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.COMPLETED)
                .filter(order -> !order.getCompletedOrderDate().isBefore(startDateTime))
                .filter(order -> !order.getCompletedOrderDate().isAfter(endDateTime))
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

        public Optional<OrderSummary> getOrderDetails(long orderId) {
        return orderService.findOrderById(orderId).map(OrderSummary::new);
    }

    public List<BookRequestSummary> getBookRequestList(SortByRequestBook sortParam) {

        Map<Book, List<BookRequest>> groupedByBook = requestService.getRequestsList().stream()
                .collect(Collectors.groupingBy(BookRequest::getReqBook));

        Comparator<BookRequestSummary> comparator = switch (sortParam) {
            case ALPHABET -> Comparator.comparing(summary -> summary.getBook().getTitle());
            case COUNT_REQUEST -> Comparator.<BookRequestSummary>comparingLong(BookRequestSummary::getRequestCount).reversed();
        };

        return groupedByBook.entrySet().stream()
                .map(entry -> new BookRequestSummary(entry.getKey(), entry.getValue()))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public List<BookSummary> getUnsoldBooksMoreThanSixMonth (SortByUnsoldBook sortParam) {

        LocalDateTime sixMonthAgo = LocalDateTime.now().minusMonths(6);

        Map<Book, LocalDateTime> lastSoldDateByBook = new HashMap<>();

        for (Order order : orderService.getOrderList()) {
            if(order.getOrderStatus() != OrderStatus.COMPLETED) continue;
            for (OrderItem orderItem : order.getOrderItemsList()) {
                Book book = orderItem.getBook();
                LocalDateTime orderDate = order.getCompletedOrderDate();
                LocalDateTime lastSoldDate = lastSoldDateByBook.get(book);
                if (lastSoldDate == null || orderDate.isAfter(lastSoldDate)) {
                    lastSoldDateByBook.put(book, orderDate);
                }
            }
        }

        Comparator<BookSummary> comparator = switch (sortParam) {
            case DELIVERY_DATE -> Comparator.comparing(BookSummary::getDeliveryDate);
            case PRICE ->  Comparator.comparing(BookSummary::getPrice).reversed();
        };

        return catalog.getBooks().stream()
                .filter(book -> {
                    LocalDateTime lastSoldDate = lastSoldDateByBook.get(book);
                    return lastSoldDate == null || lastSoldDate.isBefore(sixMonthAgo);
                })
                .map(BookSummary::new)
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
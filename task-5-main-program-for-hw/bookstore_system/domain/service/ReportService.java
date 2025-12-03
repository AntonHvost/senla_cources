package bookstore_system.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import bookstore_system.domain.exception.BusinessValidationException;
import bookstore_system.domain.model.*;
import bookstore_system.dto.*;
import bookstore_system.enums.*;

public class ReportService {
    private final OrderService orderService;
    private final RequestService requestService;
    private final ConsumerService consumerService;
    private final BookInventoryService catalog;

    public ReportService(OrderService orderService, RequestService requestService, BookInventoryService catalog, ConsumerService consumerService) {
        this.orderService = orderService;
        this.requestService = requestService;
        this.catalog = catalog;
        this.consumerService = consumerService;
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
                .map(order -> {
                            Consumer consumer = consumerService.findConsumerById(order.getConsumerId())
                                    .orElse(null);
                            List<OrderItemSummary> items = order.getOrderItemsList().stream()
                                .map(orderItem -> new OrderItemSummary(orderItem, catalog.findBookById(orderItem.getBookId())
                                    .orElse(null)))
                                .toList();
                            return new OrderSummary(order, consumer, items);
                        }
                )
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
            default -> Comparator.comparing(BookSummary::getId);
        };

        return catalog.getBooks().stream()
                .map(book -> new BookSummary(book, null))
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
                .map(order ->  {
                    Consumer consumer = consumerService.findConsumerById(order.getConsumerId()).orElse(null);
                    System.out.println(order.getConsumerId());
                    List<OrderItemSummary> items = new ArrayList<>();
                    for (OrderItem orderItem : order.getOrderItemsList()) {
                        OrderItemSummary orderItemSummary = new OrderItemSummary(orderItem, catalog.findBookById(orderItem.getBookId())
                                .orElse(null));
                        items.add(orderItemSummary);
                    }
                    return new OrderSummary(order, consumer, items);
                })
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

    public Optional<OrderSummary> getOrderDetails(Long orderId) {

    return orderService.findOrderById(orderId).map(order -> {
                Consumer consumer = consumerService.findConsumerById(order.getConsumerId()).orElse(null);

                List<OrderItemSummary> items = order.getOrderItemsList().stream()
                        .map(orderItem -> new OrderItemSummary(orderItem, catalog.findBookById(orderItem.getBookId())
                                .orElse(null)))
                        .toList();
                return new OrderSummary(order, consumer, items);
            }
        );
    }

    public List<BookRequestSummary> getBookRequestList(SortByRequestBook sortParam) {

        Map<Long, List<BookRequest>> groupedByBookId = requestService.getRequestsList().stream()
                .collect(Collectors.groupingBy(BookRequest::getReqBookId));

        List<BookRequestSummary> bookRequestSummaries = groupedByBookId.entrySet()
                .stream()
                .map(entry -> {
                    Long reqBookId = entry.getKey();
                    List<BookRequest> bookRequests = entry.getValue();

                    Book book =  catalog.findBookById(reqBookId).orElse(null);
                    if (book == null) {
                        return null;
                    }
                    return new BookRequestSummary(book, bookRequests);
                })
                .filter(Objects::nonNull)
                .toList();

        Comparator<BookRequestSummary> comparator = switch (sortParam) {
            case ALPHABET -> Comparator.comparing(summary -> summary.getBook().getTitle());
            case COUNT_REQUEST -> Comparator.<BookRequestSummary>comparingLong(BookRequestSummary::getRequestCount).reversed();
            default -> Comparator.comparing(summary -> summary.getBook().getId());
        };

        return bookRequestSummaries.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public List<BookSummary> getUnsoldBooksMoreThanSixMonth (SortByUnsoldBook sortParam) {

        LocalDateTime sixMonthAgo = LocalDateTime.now().minusMonths(6);

        Map<Long, LocalDateTime> lastSoldDateByBookId = new HashMap<>();
        for (Order order : orderService.getOrderList()) {
            if (order.getOrderStatus() != OrderStatus.COMPLETED) continue;
            for (OrderItem item : order.getOrderItemsList()) {
                Long bookId = item.getBookId();
                LocalDateTime completedDate = order.getCompletedOrderDate();
                lastSoldDateByBookId.merge(bookId, completedDate, (old, newDate) ->
                        newDate.isAfter(old) ? newDate : old);
            }
        }

        Map<Long, LocalDateTime> lastDeliveryDateByBookId = new HashMap<>();
        for (BookRequest request : requestService.getRequestsList()) {
            if (request.getStatus() == RequestStatus.FULFILLED && request.getDeliveryDate() != null) {
                Long bookId = request.getReqBookId();
                LocalDateTime deliveryDate = request.getDeliveryDate();
                lastDeliveryDateByBookId.merge(bookId, deliveryDate, (old, newDate) ->
                        newDate.isAfter(old) ? newDate : old);
            }
        }

        Comparator<BookSummary> comparator = switch (sortParam) {
            case DELIVERY_DATE -> Comparator.comparing(BookSummary::getDeliveryDate, Comparator.nullsLast(Comparator.naturalOrder()));
            case PRICE ->  Comparator.comparing(BookSummary::getPrice).reversed();
            default -> Comparator.comparing(BookSummary::getId);
        };

        return catalog.getBooks().stream()
                .filter(book -> {
                    LocalDateTime lastSoldDate = lastSoldDateByBookId.get(book.getId());
                    return lastSoldDate == null || lastSoldDate.isBefore(sixMonthAgo);
                })
                .map(book -> new BookSummary(book, lastDeliveryDateByBookId.get(book.getId())))
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
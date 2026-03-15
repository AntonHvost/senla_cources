package service;

import domain.model.impl.*;
import dto.response.*;
import jakarta.transaction.Transactional;
import mapper.ResponseDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import enums.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final OrderService orderService;
    private final RequestService requestService;
    private final BookInventoryService bookInventoryService;

    @Value("${unsoldBookMonth}")
    private int thresholdMonth;

    public ReportService(OrderService orderService, RequestService requestService, BookInventoryService bookInventoryService) {
        this.orderService = orderService;
        this.requestService = requestService;
        this.bookInventoryService = bookInventoryService;
    }

    private LocalDateTime parseToDateTime(String dateStr) {
        try {
            return LocalDate.parse(dateStr).atStartOfDay();
        } catch (DateTimeParseException e) {
            logger.warn("Invalid date format: {}", dateStr);
            throw new IllegalArgumentException("Неверный формат даты: " + dateStr);
        }
    }

    public List<OrderResponseDto> getCompletedOrdersToPeriod(String startDate, String endDate, SortByOrder sortParam) {
        logger.info("Generating completed orders report from {} to {}, sorted by {}", startDate, endDate, sortParam);

        LocalDateTime startDateTime = parseToDateTime(startDate);
        LocalDateTime endDateTime = parseToDateTime(endDate);

        Comparator<OrderResponseDto> comparator = switch (Optional.ofNullable(sortParam)
                .orElse(SortByOrder.ID)) {
            case ID -> Comparator.comparing(OrderResponseDto::getId);
            case PRICE -> Comparator.comparing(OrderResponseDto::getTotalPrice);
            case COMPLETE_DATE -> Comparator.comparing(OrderResponseDto::getCompletedOrderDate);
            default -> throw new IllegalArgumentException("Неверный параметр сортировки: " + sortParam);
        };

        List<OrderResponseDto> result = orderService.getOrderList().stream()
                .filter(o -> o.getOrderStatus() == OrderStatus.COMPLETED)
                .filter(o -> !o.getCompletedAtDate().isBefore(startDateTime))
                .filter(o -> !o.getCompletedAtDate().isAfter(endDateTime))
                .map(ResponseDtoMapper::toOrderResponseDto)
                .sorted(comparator)
                .collect(Collectors.toList());

        logger.info("Report generated: {} completed orders in period", result.size());

        return result;
    }

    public int getCompletedOrdersCount(String startDate, String endDate) {
        logger.debug("Counting completed orders from {} to {}", startDate, endDate);

        LocalDateTime startDateTime = parseToDateTime(startDate);
        LocalDateTime endDateTime = parseToDateTime(endDate);

        int count = (int) orderService.getOrderList().stream()
                .filter(o -> o.getOrderStatus() == OrderStatus.COMPLETED)
                .filter(o -> !o.getCompletedAtDate().isBefore(startDateTime))
                .filter(o -> !o.getCompletedAtDate().isAfter(endDateTime))
                .count();

        logger.info("Found {} completed orders in period {} – {}", count, startDate, endDate);

        return count;
    }

    public BookDescriptionResponseDto getDescriptionBook(long bookId) {
        logger.debug("Fetching description for book ID: {}", bookId);
        return bookInventoryService.findBookById(bookId).map(book -> ResponseDtoMapper.toBookDescriptionResponse(book.getId(), book.getDescription())).orElseGet(() -> {
            logger.warn("No description for book ID: {}", bookId);
            return ResponseDtoMapper.toBookDescriptionResponse(null, "Книга с ID " + bookId + " не существует.");
        });
    }

    public List<BookResponseDto> getBookCatalog(SortByBook sortParam) {
        logger.info("Fetching book catalog, sorted by: {}", sortParam);

        Comparator<BookResponseDto> comparator = switch (Optional.ofNullable(sortParam)
                .orElse(SortByBook.ID)) {
            case ALPHABET -> Comparator.comparing(BookResponseDto::getTitle);
            case PUBLICATION_DATE -> Comparator.comparing(BookResponseDto::getPublishDate);
            case PRICE -> Comparator.comparing(BookResponseDto::getPrice);
            case IN_STOCK -> Comparator.comparing(BookResponseDto::getStatus);
            default -> Comparator.comparing(BookResponseDto::getId);
        };

        List<BookResponseDto> result = bookInventoryService.getBooks().stream()
                .map(ResponseDtoMapper::toBookResponseDto)
                .sorted(comparator)
                .collect(Collectors.toList());

        logger.debug("Catalog contains {} books", result.size());

        return result;
    }

    public List<OrderResponseDto> getOrderList(SortByOrder sortParam) {
        logger.info("Fetching order list, sorted by: {}", sortParam);
        Comparator<OrderResponseDto> comparator = switch (Optional.ofNullable(sortParam)
                .orElse(SortByOrder.ID)) {
            case COMPLETE_DATE -> Comparator.comparing(OrderResponseDto::getCompletedOrderDate, Comparator.naturalOrder());
            case PRICE -> Comparator.comparing(OrderResponseDto::getTotalPrice);
            case STATUS -> Comparator.comparing(OrderResponseDto::getStatus);
            default -> Comparator.comparing(OrderResponseDto::getId);
        };

        logger.debug("Fetched orders");

        return orderService.getOrderList().stream()
                .map(order -> ResponseDtoMapper.toOrderResponseDto(order, null, null))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public BigDecimal getProfitToPeriod(String startDate, String endDate) {
        logger.info("Calculating profit from {} to {}", startDate, endDate);

        LocalDateTime startDateTime = parseToDateTime(startDate);
        LocalDateTime endDateTime = parseToDateTime(endDate);

        BigDecimal profit = orderService.getOrderList().stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.COMPLETED)
                .filter(order -> !order.getCompletedAtDate().isBefore(startDateTime))
                .filter(order -> !order.getCompletedAtDate().isAfter(endDateTime))
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        logger.info("Total profit in period: {}", profit);

        return profit;
    }

    public Optional<OrderResponseDto> getOrderDetails(long orderId) {
        logger.debug("Fetching details for order ID: {}", orderId);
        return orderService.findOrderDetailById(orderId)
                .map(order -> ResponseDtoMapper.toOrderResponseDto(order));
    }

    @Transactional
    public List<BookRequestSummary> getBookRequestList(SortByRequestBook sortParam) {
        logger.info("Fetching book request list, sorted by: {}", sortParam);

        List<BookRequest> requestList = requestService.findAllRequestWithBook();

        Map<Book, List<BookRequest>> groupedByBook = requestList.stream()
                .collect(Collectors.groupingBy(BookRequest::getReqBook));

        Comparator<BookRequestSummary> comparator = switch (Optional.ofNullable(sortParam)
                .orElse(SortByRequestBook.ID)) {
            case ALPHABET -> Comparator.comparing(summary -> summary.getBook().getTitle());
            case COUNT_REQUEST -> Comparator.comparingLong(BookRequestSummary::getRequestCount).reversed();
            default -> Comparator.comparing(summary -> summary.getBook().getId());
        };



        List<BookRequestSummary> result = groupedByBook.entrySet().stream()
                .map(entry -> new BookRequestSummary(ResponseDtoMapper.toBookResponseDto(entry.getKey()),
                        ResponseDtoMapper.toBookRequestResponseDtoList(entry.getValue())))
                .sorted(comparator)
                .collect(Collectors.toList());

        logger.debug("Fetched {} book request summaries", result.size());

        return result;
    }

    public List<BookSummary> getUnsoldBooksMoreThanNMonth(SortByUnsoldBook sortParam) {
        logger.info("Fetching unsold books (older than {} months), sorted by: {}", thresholdMonth, sortParam);

        LocalDateTime sixMonthAgo = LocalDateTime.now().minusMonths(thresholdMonth);

        List<Order> orderList = orderService.getOrderList();
        List<BookRequest> bookRequestList = requestService.getRequestsList();

        Map<Book, LocalDateTime> lastSoldDateByBook = new HashMap<>();
        Map<Book, LocalDateTime> lastDeliveryDateByBookId = new HashMap<>();

        for (Order order : orderList) {
            if(order.getOrderStatus() != OrderStatus.COMPLETED) continue;
            for (OrderItem orderItem : order.getOrderItemsList()) {
                Book book = orderItem.getBook();
                LocalDateTime orderDate = order.getCompletedAtDate();
                LocalDateTime lastSoldDate = lastSoldDateByBook.get(book);
                if (lastSoldDate == null || orderDate.isAfter(lastSoldDate)) {
                    lastSoldDateByBook.put(book, orderDate);
                }
            }
        }

        for (BookRequest request : bookRequestList) {
            if (request.getStatus() == RequestStatus.FULFILLED && request.getDeliveryDate() != null) {
                Book book = request.getReqBook();
                LocalDateTime deliveryDate = request.getDeliveryDate();
                lastDeliveryDateByBookId.merge(book, deliveryDate, (old, newDate) ->
                        newDate.isAfter(old) ? newDate : old);
            }
        }

        Comparator<BookSummary> comparator = switch (Optional.ofNullable(sortParam)
                .orElse(SortByUnsoldBook.ID)) {
            case DELIVERY_DATE -> Comparator.comparing(BookSummary::getDeliveryDate);
            case PRICE ->  Comparator.comparing(BookSummary::getPrice).reversed();
            default -> Comparator.comparing(BookSummary::getId);
        };

        List<BookSummary> result = bookInventoryService.getBooks().stream()
                .filter(book -> {
                    LocalDateTime lastSoldDate = lastSoldDateByBook.get(book);
                    return lastSoldDate == null || lastSoldDate.isBefore(sixMonthAgo);
                })
                .map(book -> new BookSummary(book, lastDeliveryDateByBookId.get(book)))
                .sorted(comparator)
                .collect(Collectors.toList());

        logger.debug("Found {} unsold books", result.size());

        return result;
    }
}
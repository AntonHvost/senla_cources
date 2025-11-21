package t1.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import t1.enums.*;

import t1.model.*;

public class ReportService {
    private OrderService orderService;
    private RequestService requestService;
    private BookCatalog catalog;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ReportService(OrderService orderService, RequestService requestService, BookCatalog catalog) {
        this.orderService = orderService;
        this.requestService = requestService;
        this.catalog = catalog;
    }

    public String viewCompletedOrdersToPeriod(String startDate, String endDate, SortByOrder sortParam) {
        Comparator<Order> comparator = switch (sortParam) {
                    case PRICE -> Comparator.comparing(Order::getTotalPrice);
                    case COMPLETE_DATE -> Comparator.comparing(Order::getCompletedOrderDate);
            default -> null;
                };
        try {
        assert comparator != null;
        return orderService.getOrderList().stream()
                .filter(o -> o.getOrderStatus() == OrderStatus.COMPLETED)
                .filter(o -> !o.getCompletedOrderDate().isBefore(LocalDateTime.parse(startDate)))
                .filter(o -> !o.getCompletedOrderDate().isAfter(LocalDateTime.parse(endDate)))
                .sorted(comparator)
                .map(order ->
                        "ID заказа: " + order.getId() + "\n" +
                                "Заказчик: " + order.getConsumer().getName() + "\n" +
                                "Дата завершения заказа: " + order.getCompletedOrderDate().format(formatter) + "\n" +
                                "Цена заказа: " + order.getTotalPrice() + "\n" +
                                "Статус заказа: " + order.getOrderStatus() + "\n")
                .collect(Collectors.joining());
        } catch (NullPointerException e) {
            return "Неверный параметр сортировки!";
        }
    }

    public int viewCompletedOrdersCount(String startDate, String endDate) {
        return (int) orderService.getOrderList().stream()
                .filter(o -> o.getOrderStatus() == OrderStatus.COMPLETED)
                .filter(o -> !o.getCompletedOrderDate().isBefore(LocalDateTime.parse(startDate)))
                .filter(o -> !o.getCompletedOrderDate().isAfter(LocalDateTime.parse(endDate)))
                .count();
    }

    public String viewDescriptionBook(long bookId) {
        return catalog.findBookById(bookId).get().getDescription();
    }

    public String viewBookCatalog(SortByBook sortParam) {
        List<Book> books = catalog.getBooks();
        Comparator<Book> comparator = switch (sortParam) {
            case ALPHABET -> Comparator.comparing(Book::getTitle);
            case PUBLICATION_DATE -> Comparator.comparing(Book::getPublishDate);
            case PRICE -> Comparator.comparing(Book::getPrice);
            case IN_STOCK -> Comparator.comparing(Book::getStatus);
        };

        return books.stream()
                .sorted(comparator)
                .map(book -> {
                    String inStock = book.getStatus() == BookStatus.AVAILABLE ? "В наличии" : "Отсутствует";
                    return " - " + book.getTitle() +
                        " (Автор: " + book.getAuthor() + ")\n" +
                        "Описание: " + book.getDescription() + "\n" +
                        "Дата публикации: " + book.getPublishDate() + "\n" +
                        "Цена: " + book.getPrice() + "\n" +
                            "Статус: " + inStock + "\n\n";
                })
                .collect(Collectors.joining());
    }

    public String viewOrderList(SortByOrder sortParam) {
        Comparator<Order> comparator = switch (sortParam) {
            case COMPLETE_DATE -> Comparator.comparing(Order::getCompletedOrderDate, Comparator.naturalOrder());
            case PRICE -> Comparator.comparing(Order::getTotalPrice);
            case STATUS -> Comparator.comparing(Order::getOrderStatus);
        };

        return orderService.getOrderList().stream()
                .sorted(comparator)
                .map(order -> {
                    order.getCompletedOrderDate().format(formatter);
                    String completedDate = order.getCompletedOrderDate().toString();
                    return "ID заказа: " + order.getId() + "\n" +
                                "Заказчик: " + order.getConsumer().getName() + "\n" +
                                "Дата открытия заказа " + order.getCreatedOrderDate() + "\n" +
                                "Дата завершения заказа: " + completedDate + "\n" +
                                "Цена заказа: " + order.getTotalPrice() + "\n" +
                                "Статус заказа: " + order.getOrderStatus() + "\n";
                })
                .collect(Collectors.joining());
    }

    public BigDecimal viewProfitToPeriod(String startDate, String endDate) {
        return orderService.getOrderList().stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.COMPLETED)
                .filter(order -> !order.getCompletedOrderDate().isBefore(LocalDateTime.parse(startDate)))
                .filter(order -> !order.getCompletedOrderDate().isAfter(LocalDateTime.parse(endDate)))
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    public String viewOrderDetails(long orderId) {
        return orderService.findOrderById(orderId)
                .map(order -> {
                    StringBuilder details = new StringBuilder();
                    Consumer consumer = order.getConsumer();
                    if (consumer != null) {
                        details.append("Данные заказчика:\n");
                        details.append("Имя: ").append(consumer.getName()).append("\n");
                        details.append("Номер телефона: ").append(consumer.getPhone()).append("\n");
                        details.append("Email: ").append(consumer.getEmail()).append("\n");
                    } else {
                        details.append("Данные заказчика отсутствуют.\n");
                    }

                    details.append("Книги заказа:\n");
                    for (OrderItem i : order.getOrderItemsList()) {
                        Book book = i.getBook();
                        details.append("  - ").append(book.getTitle())
                                .append(" (Автор: ").append(book.getAuthor()).append(")")
                                .append(", Цена: ").append(book.getPrice()).append("\n");
                    }

                    return details.toString();
                })
                .orElse("Заказ с ID " + orderId + " не найден.");
    }

    public String viewBookRequestList(SortByRequestBook sortParam) {

        Map<Book, List<BookRequest>> groupedByBook = requestService.getRequestsList().stream()
                .collect(Collectors.groupingBy(BookRequest::getReqBook));

        Comparator<Book> comparator = switch (sortParam) {
            case ALPHABET -> Comparator.comparing(Book::getTitle);
            case COUNT_REQUEST -> {
                yield Comparator.<Book>comparingLong(book -> (long) groupedByBook.getOrDefault(book, List.of()).size())
                        .reversed();
            }
        };

        List<Book> sortedBooks = groupedByBook.keySet().stream()
                .sorted(comparator)
                .toList();

        return sortedBooks.stream()
                .map(book -> {
                    List<BookRequest> bookRequests = groupedByBook.get(book);
                    long count = bookRequests.size();
                    String bookInfo = " - " + book.getTitle() + " (Запросов на данную книгу: " + count + ")\n";
                    String requestInfo = bookRequests.stream()
                            .map(req -> "   • Дата: " + req.getRequestDate().format(formatter) +
                                    ", Статус: " + req.getStatus() +
                                    (req.getRelatedOrder() != null ?
                                            " (Заказ ID: " + req.getRelatedOrder().getId() + ")" :
                                            " (Без заказа)") +
                                    "\n")
                            .collect(Collectors.joining(""));
                    return bookInfo + requestInfo;
                })
                .collect(Collectors.joining());
    }

    public String viewUnsoldBooksMoreThanSixMonth (SortByUnsoldBook sortParam) {

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

        Comparator<Book> comparator = switch (sortParam) {
            case DELIVERY_DATE -> Comparator.comparing(Book::getDeliveryDate);
            case PRICE ->  Comparator.comparing(Book::getPrice).reversed();
        };

        return catalog.getBooks().stream()
                .filter(book -> {
                    LocalDateTime lastSoldDate = lastSoldDateByBook.get(book);
                    return lastSoldDate == null || lastSoldDate.isBefore(sixMonthAgo);
                })
                .sorted(comparator)
                .map(book -> {
                    LocalDateTime lastSoldDate = lastSoldDateByBook.get(book);
                    StringBuilder details = new StringBuilder();
                    details.append(" - ").append(book.getTitle())
                            .append(" (Автор: ").append(book.getAuthor()).append(")\n");
                    details.append("  Дата публикации: ").append(book.getPublishDate()).append("\n");
                    details.append(" Последняя дата поступления: ").append(book.getDeliveryDate().format(formatter)).append("\n");
                    details.append("  Цена: ").append(book.getPrice()).append("\n");
                    if (lastSoldDate != null) {
                        details.append("  Последняя продажа: ").append(lastSoldDate).append("\n");
                    } else {
                        details.append("  Последняя продажа: Никогда не продавалась\n");
                    }
                    details.append("---\n");
                    return details.toString();
                })
                .collect(Collectors.joining());
    }
}
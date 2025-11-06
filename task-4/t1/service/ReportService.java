package t1.service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import t1.enums.OrderStatus;
import t1.enums.SortByBook;
import t1.enums.SortByOrder;
import t1.model.*;
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
                .map( book -> {
                    StringBuilder details = new StringBuilder();
                    details.append(" - ").append(book.getTitle())
                            .append(" (Автор: ").append(book.getAuthor()).append(")\n");
                    details.append("Описание: ").append(book.getDescription()).append("\n");
                    details.append("Дата публикации: ").append(book.getPublishDate()).append("\n");
                    details.append("Цена: ").append(book.getPrice()).append("\n");
                    return details.toString();
                })
                .collect(Collectors.joining());
    }

    public String viewOrderList(SortByOrder sortParam) {
        Comparator<Order> comparator = switch (sortParam) {
            case COMPLETE_DATE -> Comparator.comparing(Order::getOrderDate);
            case PRICE -> Comparator.comparing(Order::getTotalPrice);
            case STATUS -> Comparator.comparing(Order::getOrderStatus);
        };

        return orderService.getOrderList().stream()
                .sorted(comparator)
                .map(order -> {
                    StringBuilder details = new StringBuilder();
                    details.append("ID заказа: ").append(order.getId()).append("\n");
                    details.append("Заказчик: ").append(order.getConsumer().getName()).append("\n");
                    details.append("Дата заказа: ").append(order.getOrderDate()).append("\n");
                    details.append("Цена заказа: ").append(order.getTotalPrice()).append("\n");
                    details.append("Статус заказа: ").append(order.getOrderStatus()).append("\n");
                    return details.toString();
                })
                .collect(Collectors.joining());
    }

    public BigDecimal getProfitToPeriod(String startDate, String endDate) {
        return orderService.getOrderList().stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.COMPLETED)
                .filter(order -> !order.getOrderDate().isBefore(LocalDateTime.parse(startDate)))
                .filter(order -> !order.getOrderDate().isAfter(LocalDateTime.parse(endDate)))
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    public String getOrderDetails(long orderId) {
        return orderService.findOrderById(orderId)
                .map(order -> {
                    StringBuilder details = new StringBuilder();
                    Consumer consumer = order.getConsumer();
                    details.append("Заказ ID: ").append(order.getId()).append("\n");
                    details.append("Дата заказа: ").append(order.getOrderDate()).append("\n");
                    details.append("Статус заказа: ").append(order.getOrderStatus()).append("\n");
                    if(consumer != null) {
                        details.append("Данные заказчика:\n");
                        details.append("Имя: ").append(consumer.getName()).append("\n");
                        details.append("Номер телефона: ").append(consumer.getPhone()).append("\n");
                        details.append("Email: ").append(consumer.getEmail()).append("\n");
                    }
                    else {
                        details.append("Данные заказчика отсутствуют.\n");
                    }

                    details.append("Книги заказа:\n");
                    for (OrderItem i: order.getOrderItemsList()) {
                        Book book = i.getBook();
                        details.append("  - ").append(book.getTitle())
                                .append(" (Автор: ").append(book.getAuthor()).append(")")
                                .append(", Цена: ").append(book.getPrice()).append("\n");
                    }

                    return details.toString();
                })
                .orElse("Заказ с ID " + orderId + " не найден.");
    }
}

package bookstore_system.ui.controller;

import bookstore_system.domain.Consumer;
import bookstore_system.domain.Order;
import bookstore_system.domain.OrderItem;
import bookstore_system.dto.OrderSummary;
import bookstore_system.enums.OrderStatus;
import bookstore_system.enums.SortByOrder;
import bookstore_system.facade.OrderFacade;
import bookstore_system.facade.ReportFacade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class OrderController {

    private final OrderFacade orderFacade;
    private final ReportFacade reportFacade;
    private final Scanner scanner = new Scanner(System.in);

    public OrderController(OrderFacade orderFacade, ReportFacade reportFacade) {
        this.orderFacade = orderFacade;
        this.reportFacade = reportFacade;
    }

    public void createOrder() {
        System.out.println("Создание заказа");
        System.out.println("Данные заказчика: ");
        System.out.println("Имя: ");
        String username = scanner.nextLine();
        System.out.println("Контактный телефон: ");
        String phoneNumber = scanner.nextLine();
        System.out.println("Почта: ");
        String email = scanner.nextLine();

        Consumer consumer = new Consumer(username, phoneNumber, email);

        System.out.println("Введите ID книг и их количество через пробел (например, '1 10, 3 5'). Введите 'end', чтобы закончить");
        String input = scanner.nextLine().trim();

        List<Long> bookIds = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        for (String pair : input.split(",")) {

            pair = pair.trim();
            String[] parts = pair.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Неверный формат ввода!");
                continue;
            }

            try {
                bookIds.add(Long.parseLong(parts[0]));
                quantities.add(Integer.parseInt(parts[1]));
            }
            catch (NumberFormatException e) {
                System.out.println("Неверный формат числа. Попробуйте снова!");
            }
        }

        if (bookIds.isEmpty()) {
            System.out.println("Не указаны книги для заказа!");
        }

        long[] bookIdsArray = bookIds.stream().mapToLong(Long::longValue).toArray();
        int[] quantitiesArray = quantities.stream().mapToInt(Integer::intValue).toArray();

        try {
             Order order = orderFacade.createOrder(bookIdsArray, quantitiesArray, consumer);
            System.out.println("Заказ успешно создан под номером " + order.getId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }

    public void cancelOrder() {
        System.out.println("Введите номер заказа:");
        orderFacade.cancelOrder(scanner.nextLong());
        System.out.println("Заказ успешно отменён!");
    }

    public void changeOrderStatus() {
        System.out.println("Введите номер заказа:");

        long orderId = Long.parseLong(scanner.nextLine().trim());

        System.out.println("Выберите новый статус из предложенного списка:");

        int i = 1;

        for(OrderStatus status : OrderStatus.values()) {
            System.out.println((i++) + ". " + status);
        }

        switch (scanner.nextLine().trim()) {
            case "1" -> orderFacade.updStatusOrder(orderId, OrderStatus.NEW);
            case "2" -> orderFacade.updStatusOrder(orderId, OrderStatus.IN_PROCESS);
            case "3" -> orderFacade.updStatusOrder(orderId, OrderStatus.COMPLETED);
            case "4" -> orderFacade.updStatusOrder(orderId, OrderStatus.CANCELLED);
        }

        System.out.println("Статус успешно изменен!");
    }

    public void completeOrder() {
        System.out.println("Введите номер заказа:");
        if(orderFacade.completeOrder(Long.parseLong(scanner.nextLine().trim()))) System.out.println("Заказ успешно завершен!");
        else System.out.println("Заказ не может быть завершён!");
    }

    public void showOrder() {
        List<OrderSummary> orderSummaryList = new ArrayList<>();

        System.out.println("Введите предпочитаемую сортировку: ");
        System.out.println(
                "1. Сортировка по дате завершения;\n" +
                "2. Сортировка по цене;\n" +
                "3. Сортировка по статусу."
        );

        switch (scanner.nextLine().trim()) {
            case "1" -> reportFacade.getOrderList(SortByOrder.COMPLETE_DATE);
            case "2" -> reportFacade.getOrderList(SortByOrder.PRICE);
            case "3" -> reportFacade.getOrderList(SortByOrder.STATUS);
            default -> reportFacade.getOrderList(SortByOrder.ID);
        }

        if(orderSummaryList.isEmpty()) {
            System.out.println("Список заказов пуст.\n");
        } else {
            System.out.println("Список заказов:\n");

            orderSummaryList.stream().forEach(order -> System.out.println(
                    "ID заказа: " + order.getId() + "\n"
                            + "Дата создания: " + order.getCreatedOrderDate() + "\n"
                            + "Дата завершения заказа: " + (order.getCompletedOrderDate() != null ? order.getCompletedOrderDate() : "Не завершён") + "\n"
                            + "Цена заказа: " +  order.getPrice() + "\n"
                            + "Статус: " + order.getStatus() + "\n"
            ));
        }


    }

    public void showOrderDetails() {
        System.out.println("Введите номер заказа:");
        Optional<OrderSummary> currentOrder = reportFacade.getOrderDetails(Long.parseLong(scanner.nextLine().trim()));
        System.out.println(currentOrder);

        if (currentOrder.isEmpty()) {
            System.out.println("Данный заказ отсутствует!\n");
        } else {
            System.out.println("Детали заказа №" + currentOrder.get().getId());
            currentOrder.ifPresent(order -> {
                Consumer consumer = order.getConsumer();
                List<OrderItem> items = order.getOrderItemList();
                System.out.println("" +
                        "Дата создания заказа: " + order.getCreatedOrderDate() + "\n"
                        + "Дата завершения заказа: " + (order.getCompletedOrderDate() == null ? "Отсутствует \n" : (order.getCompletedOrderDate() + "\n")
                ));
                System.out.println("=Информация о заказчике=\n");
                System.out.println(
                        "Имя: " + consumer.getName() + "\n"
                                + "Контактный телефон: " + consumer.getEmail() + "\n"
                                + "Почта: " + consumer.getEmail() + "\n"
                );
                System.out.println("=Детали заказа=\n");
                items.stream().forEach(orderItem -> System.out.println(
                        "- Название книги: " + orderItem.getBook().getTitle() + "\n"
                                + "- Цена: " + orderItem.getBook().getPrice() + "\n"
                                + "- Количество: " + orderItem.getQuantity() +"\n"
                ));
                System.out.println("Итоговая цена: " + order.getPrice());
                System.out.println("Статус заказа: " + order.getStatus() + "\n");
            });
        }
    }

}

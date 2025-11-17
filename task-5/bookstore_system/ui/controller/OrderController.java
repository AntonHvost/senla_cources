package bookstore_system.ui.controller;

import bookstore_system.domain.Consumer;
import bookstore_system.domain.Order;
import bookstore_system.domain.OrderItem;
import bookstore_system.dto.OrderSummary;
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

    public void completeOrder() {
        System.out.println("Введите номер заказа:");
        orderFacade.completeOrder(scanner.nextLong());
        System.out.println("Заказ успешно завершен!");
    }

    public void showOrder() {
        System.out.println("Список заказов:\n");
        reportFacade.getOrderList(SortByOrder.PRICE).stream().forEach(order -> System.out.println(
                "ID заказа: " + order.getId() + "\n"
                + "Дата создания: " + order.getCreatedOrderDate() + "\n"
                + "Дата завершения заказа: " + (order.getCompletedOrderDate() != null ? "Не завершён" : order.getCompletedOrderDate()) + "\n"
                + "Цена заказа: " +  order.getPrice() + "\n"
                + "Статус: " + order.getStatus()
        ));
    }

    public void showOrderDetails() {
        System.out.println("Введите номер заказа:");
        Optional<OrderSummary> currentOrder = reportFacade.getOrderDetails(scanner.nextLong());
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

    public void showCompletedOrderAtPeriod() {
        System.out.println("Введите начальную дату формата 'год-месяц-число':");
        String startDate = scanner.nextLine().trim();
        System.out.println("Введите конечную дату формата 'год-месяц-число':");
        String endDate = scanner.nextLine().trim();

        System.out.println("Выберите желаемую сортировку: ");
        System.out.println(
                "1. Сортировка по дате завершения;" +
                        "2. Сортировка по цене."
        );

        List<OrderSummary> orderList = new ArrayList<>();

        switch (scanner.nextLine()) {
            case "1" -> {
                orderList = reportFacade.getCompletedOrdersAtPeriod(startDate, endDate, SortByOrder.COMPLETE_DATE);
                break;
            }
            case "2" -> {
                orderList = reportFacade.getCompletedOrdersAtPeriod(startDate, endDate, SortByOrder.PRICE);
                break;
            }
        }

        orderList.stream().forEach(order -> System.out.println(
                    "ID заказа: " + order.getId() + "\n"
                            + "Дата создания: " + order.getCreatedOrderDate() + "\n"
                            + "Дата завершения заказа: " + order.getCompletedOrderDate() + "\n"
                            + "Цена заказа: " +  order.getPrice() + "\n"
                            + "Статус: " + order.getStatus()
        ));

    }

    public void showCompletedOrderCountAtPeriod() {
        System.out.println("Введите начальную дату формата 'год-месяц-число':");
        String startDate = scanner.nextLine().trim();
        System.out.println("Введите конечную дату формата 'год-месяц-число':");
        String endDate = scanner.nextLine().trim();
        System.out.println("Количество завершённых заказов за выбранный период: " + reportFacade.getCountCompletedOrdersAtPeriod(startDate, endDate));
    }

    public void showProfitAtPeriod() {
        System.out.println("Введите начальную дату формата 'год-месяц-число': ");
        String startDate = scanner.nextLine().trim();
        System.out.println("Введите конечную дату формата 'год-месяц-число': ");
        String endDate = scanner.nextLine().trim();
        System.out.println("Количество заработанной суммы за данный период: " + reportFacade.getProfitAtPeriod(startDate, endDate));
    }

}

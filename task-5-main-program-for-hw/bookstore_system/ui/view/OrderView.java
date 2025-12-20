package bookstore_system.ui.view;

import bookstore_system.di.annotation.Component;
import bookstore_system.di.annotation.Inject;
import bookstore_system.domain.model.Consumer;
import bookstore_system.domain.model.Order;
import bookstore_system.dto.OrderItemSummary;
import bookstore_system.dto.OrderSummary;
import bookstore_system.enums.OrderStatus;
import bookstore_system.enums.SortByOrder;
import bookstore_system.ui.controller.OrderController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class OrderView {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9\\s\\-()]{7,15}$"
    );

    private final OrderController orderController;

    private final Scanner scanner = new Scanner(System.in);

    @Inject
    public OrderView(OrderController orderController) {
        this.orderController = orderController;
    }

    public void showCreateOrderMenu() {
        System.out.println("Создание заказа");
        System.out.println("Данные заказчика: ");

        String username = readNonEmptyString("Имя: ");
        String phoneNumber = readValidatedString("Контактный телефон: ", this::isValidPhone, "Неверный формат номера.");
        String email = readValidatedString("Почта: ", this::isValidEmail, "Неверный формат почты.");

        Consumer consumer = new Consumer(username, phoneNumber, email);

        System.out.println("Введите ID книг и их количество через пробел (например, '1 10, 3 5'). Введите 'end', чтобы закончить");
        String input = scanner.nextLine().trim();

        if ("end".equalsIgnoreCase(input)) {
            System.out.println("Заказ отменён.");
            return;
        }

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
            Order order = orderController.createOrder(bookIdsArray, quantitiesArray, consumer);
            System.out.println("Заказ успешно создан под номером " + order.getId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showCancelOrderMenu() {
        System.out.println("Введите номер заказа:");
        orderController.cancelOrder(scanner.nextLong());
        System.out.println("Заказ успешно отменён!");
    }

    public void showChangeOrderStatusMenu() {
        System.out.println("Введите номер заказа:");

        long orderId = Long.parseLong(scanner.nextLine().trim());

        System.out.println("Выберите новый статус из предложенного списка:");

        int i = 1;

        for(OrderStatus status : OrderStatus.values()) {
            System.out.println((i++) + ". " + status);
        }

        switch (scanner.nextLine().trim()) {
            case "1" -> orderController.changeOrderStatus(OrderStatus.NEW, orderId);
            case "2" -> orderController.changeOrderStatus(OrderStatus.IN_PROCESS, orderId);
            case "3" -> orderController.changeOrderStatus(OrderStatus.COMPLETED, orderId);
            case "4" -> orderController.changeOrderStatus(OrderStatus.CANCELLED, orderId);
        }

        System.out.println("Статус успешно изменен!");
    }

    public void showCompleteOrderMenu() {
        System.out.println("Введите номер заказа:");
        if(orderController.completeOrder(Long.parseLong(scanner.nextLine().trim()))) System.out.println("Заказ успешно завершен!");
        else System.out.println("Заказ не может быть завершён!");
    }

    public void showSortedOrdersMenu() {
        List<OrderSummary> orderSummaryList = new ArrayList<>();

        System.out.println("Введите предпочитаемую сортировку: ");
        System.out.println(
                "1. Сортировка по дате завершения;\n" +
                        "2. Сортировка по цене;\n" +
                        "3. Сортировка по статусу."
        );

        try {

            switch (scanner.nextLine().trim()) {
                case "1" -> orderSummaryList = orderController.getSortedOrders(SortByOrder.COMPLETE_DATE);
                case "2" -> orderSummaryList = orderController.getSortedOrders(SortByOrder.PRICE);
                case "3" -> orderSummaryList = orderController.getSortedOrders(SortByOrder.STATUS);
                default -> orderSummaryList = orderController.getSortedOrders(SortByOrder.ID);
            }
        } catch (NullPointerException e) {
            System.out.println("Данная сортировка недоступна на данный момент!");
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

    public void showOrderDetailsMenu() {
        System.out.println("Введите номер заказа:");
        Optional<OrderSummary> currentOrder = orderController.getOrder(Long.parseLong(scanner.nextLine().trim()));

        if (currentOrder.isEmpty()) {
            System.out.println("Данный заказ отсутствует!\n");
        } else {
            System.out.println("Детали заказа №" + currentOrder.get().getId());
            currentOrder.ifPresent(order -> {
                Consumer consumer = order.getConsumer();
                List<OrderItemSummary> items = order.getOrderItemList();

                System.out.println("" +
                        "Дата создания заказа: " + order.getCreatedOrderDate() + "\n"
                        + "Дата завершения заказа: " + (order.getCompletedOrderDate() == null ? "Отсутствует \n" : (order.getCompletedOrderDate() + "\n")
                ));
                System.out.println("=Информация о заказчике=\n");
                if (consumer != null) {
                    System.out.println(
                            "Имя: " + consumer.getName() + "\n"
                                    + "Контактный телефон: " + consumer.getPhone() + "\n"
                                    + "Почта: " + consumer.getEmail() + "\n"
                    );
                } else System.out.println("Заказчик отсутствует или был удалён!\n");

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

    public void showOrderImportMenu () {
        System.out.println("Введите название файла: ");
        System.out.println("Рабочая папка: " + System.getProperty("user.dir"));
        String fileName = scanner.nextLine().trim();
        try {
            orderController.importOrderFromCsv(fileName + ".csv", fileName + "_item.csv");
            System.out.println("Импорт заказов успешно завершён.");
        } catch (RuntimeException e) {
            System.out.println("Ошибка импорта из файла " + fileName + ". Проверьте экспортируемые данные или название файла на корректность.\n");
        }
    }

    public void showOrderExportMenu () {
        System.out.println("Введите название экспортируемого файла: ");
        String fileName = scanner.nextLine().trim();
        try {
            orderController.exportOrder(fileName + ".csv", fileName + "_item.csv");
            System.out.println("Заказы успешно экспортированы!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка экспорта в " + fileName + ". Проверьте название файла на корректность.\n");
        }
    }

    private String readNonEmptyString(String str) {
        while (true) {
            System.out.println(str);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Поле не может быть пустым. Введите имя заново.");
        }
    }

    private String readValidatedString(String str, Predicate<String> validator, String errorMessage) {
        while (true) {
            System.out.println(str);
            String input = scanner.nextLine().trim();
            if (validator.test(input)) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }

    private boolean isValidPhone(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}

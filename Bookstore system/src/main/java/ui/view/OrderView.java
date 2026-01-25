package ui.view;

import di.annotation.Component;
import di.annotation.Inject;
import domain.model.Consumer;
import domain.model.Order;
import dto.OrderItemSummary;
import dto.OrderSummary;
import enums.OrderStatus;
import enums.SortByOrder;
import ui.controller.OrderController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class OrderView {

    private static final Logger logger = LoggerFactory.getLogger(OrderView.class);

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
        logger.info("Initialized creating a new order");
        System.out.println("Создание заказа");
        System.out.println("Данные заказчика: ");

        String username = readNonEmptyString("Имя (или exit для возврата в меню): ");

        if ("exit".equalsIgnoreCase(username)) {
            System.out.println("Заказ отменён.");
            return;
        }

        String phoneNumber = readValidatedString("Контактный телефон: ", this::isValidPhone, "Неверный формат номера.");
        String email = readValidatedString("Почта: ", this::isValidEmail, "Неверный формат почты.");

        Consumer consumer = new Consumer(username, phoneNumber, email);

        System.out.println("Введите ID книг и их количество через пробел (например, '1 10, 3 5'): ");
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
            Order order = orderController.createOrder(bookIdsArray, quantitiesArray, consumer);
            System.out.println("Заказ успешно создан под номером " + order.getId());
            logger.info("Order ID {} created successfully", order.getId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            logger.warn("Order creation failed: {}", e.getMessage());
        }
    }

    public void showCancelOrderMenu() {
        System.out.println("Введите номер заказа:");
        Long  orderId = scanner.nextLong();

        orderController.cancelOrder(orderId);
        logger.info("Cancelled order ID={}", orderId);
        System.out.println("Заказ успешно отменён!");
    }

    public void showChangeOrderStatusMenu() {
        System.out.println("Введите номер заказа:");

        Long orderId = Long.parseLong(scanner.nextLine().trim());
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

        logger.info("Changed status of order ID={}", orderId);
        System.out.println("Статус успешно изменен!");
    }

    public void showCompleteOrderMenu() {
        logger.info("Request to complete an order");

        System.out.println("Введите номер заказа:");
        Long orderId = Long.parseLong(scanner.nextLine().trim());

        if(orderController.completeOrder(orderId)) {
            logger.info("Order ID {} completed", orderId);
            System.out.println("Заказ успешно завершен!");
        } else {
            logger.warn("Failed to complete order ID {}", orderId);
            System.out.println("Заказ не может быть завершён!");
        }
    }

    public void showSortedOrdersMenu() {
        logger.info("Requested sorted order list");
        List<OrderSummary> orderSummaryList = new ArrayList<>();

        System.out.println("Введите предпочитаемую сортировку: ");
        System.out.println(
                "1. Сортировка по дате завершения;\n" +
                        "2. Сортировка по цене;\n" +
                        "3. Сортировка по статусу."
        );

        String choice = scanner.nextLine().trim();
        logger.debug("Order sorting option: {}", choice);
        try {
            switch (choice) {
                case "1" -> orderSummaryList = orderController.getSortedOrders(SortByOrder.COMPLETE_DATE);
                case "2" -> orderSummaryList = orderController.getSortedOrders(SortByOrder.PRICE);
                case "3" -> orderSummaryList = orderController.getSortedOrders(SortByOrder.STATUS);
                default -> orderSummaryList = orderController.getSortedOrders(SortByOrder.ID);
            }
        } catch (NullPointerException e) {
            logger.error("Error fetching sorted orders", e);
            System.out.println("Данная сортировка недоступна на данный момент!");
        }

        if(orderSummaryList.isEmpty()) {
            logger.info("No orders to display");
            System.out.println("Список заказов пуст.\n");
        } else {
            logger.info("Displaying {} orders", orderSummaryList.size());
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
        logger.info("Requested order details");
        System.out.println("Введите номер заказа:");

        try {
            Long orderId = Long.parseLong(scanner.nextLine().trim());

            Optional<OrderSummary> currentOrder = orderController.getOrder(orderId);

            if (currentOrder.isEmpty()) {
                logger.warn("Order ID {} not found", orderId);
                System.out.println("Данный заказ отсутствует!\n");
            } else {
                logger.info("Displayed details for order ID {}", orderId);
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
                                    + "- Количество: " + orderItem.getQuantity() + "\n"
                    ));
                    System.out.println("Итоговая цена: " + order.getPrice());
                    System.out.println("Статус заказа: " + order.getStatus() + "\n");
                });
            }
        } catch (Exception e) {
            logger.error("Error displaying order details", e);
            System.out.println("Ошибка отображения деталей заказа");
        }
    }

    public void showOrderImportMenu () {
        logger.info("Requested order import");
        System.out.println("Введите название файла: ");
        System.out.println("Рабочая папка: " + System.getProperty("user.dir"));
        String fileName = scanner.nextLine().trim();
        logger.info("Importing orders from: {}.csv", fileName);
        try {
            orderController.importOrderFromCsv(fileName + ".csv", fileName + "_item.csv");
            System.out.println("Импорт заказов успешно завершён.");
            logger.info("Orders successfully imported from '{}.csv'", fileName);
        } catch (RuntimeException e) {
            logger.error("Failed to import orders from '{}.csv'", fileName, e);
            System.out.println("Ошибка импорта из файла " + fileName + ". Проверьте экспортируемые данные или название файла на корректность.\n");
        }
    }

    public void showOrderExportMenu () {
        logger.info("User initiated order export");
        System.out.println("Введите название экспортируемого файла: ");
        String fileName = scanner.nextLine().trim();
        logger.debug("Exporting orders to: {}.csv", fileName);
        try {
            orderController.exportOrder(fileName + ".csv", fileName + "_item.csv");
            System.out.println("Заказы успешно экспортированы!");
            logger.info("Orders successfully exported to '{}.csv'", fileName);
        } catch (RuntimeException e) {
            logger.error("Failed to export orders to '{}.csv'", fileName, e);
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

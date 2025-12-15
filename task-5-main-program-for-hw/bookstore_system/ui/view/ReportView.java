package bookstore_system.ui.view;

import bookstore_system.di.annotation.Component;
import bookstore_system.di.annotation.Inject;
import bookstore_system.dto.OrderSummary;
import bookstore_system.enums.SortByOrder;
import bookstore_system.ui.controller.ReportController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class ReportView {

    private final ReportController reportController;

    private final Scanner scanner = new Scanner(System.in);

    @Inject
    public ReportView(ReportController reportController) {
        this.reportController = reportController;
    }

    public void showCompletedOrderAtPeriodMenu() {
        System.out.println("Введите начальную дату формата 'год-месяц-число':");
        String startDate = scanner.nextLine().trim();
        System.out.println("Введите конечную дату формата 'год-месяц-число':");
        String endDate = scanner.nextLine().trim();

        System.out.println("Выберите желаемую сортировку: ");
        System.out.println(
                "1. Сортировка по дате завершения;\n" +
                        "2. Сортировка по цене."
        );

        List<OrderSummary> orderList = new ArrayList<>();
        try {
            switch (scanner.nextLine()) {
                case "1" -> {
                    orderList = reportController.getCompletedOrderAtPeriod(startDate, endDate, SortByOrder.COMPLETE_DATE);
                    break;
                }
                case "2" -> {
                    orderList = reportController.getCompletedOrderAtPeriod(startDate, endDate, SortByOrder.PRICE);
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный формат даты. Используйте формат: год-месяц-число.");
            return;
        }

        if (orderList.isEmpty()) {
            System.out.println("Нет завершенных заказов!\n");
        } else {
            orderList.stream().forEach(order -> System.out.println(
                    "ID заказа: " + order.getId() + "\n"
                            + "Дата создания: " + order.getCreatedOrderDate() + "\n"
                            + "Дата завершения заказа: " + order.getCompletedOrderDate() + "\n"
                            + "Цена заказа: " +  order.getPrice() + "\n"
                            + "Статус: " + order.getStatus()
            ));
        }

    }

    public void showCompletedOrderCountAtPeriodMenu() {
        System.out.println("Введите начальную дату формата 'год-месяц-число':");
        String startDate = scanner.nextLine().trim();
        System.out.println("Введите конечную дату формата 'год-месяц-число':");
        String endDate = scanner.nextLine().trim();
        try {
            System.out.println("Количество завершённых заказов за выбранный период: " + reportController.getCountCompletedOrdersAtPeriod(startDate, endDate));
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный формат даты. Используйте формат: год-месяц-число.");
        }
    }

    public void showProfitAtPeriodMenu() {
        System.out.println("Введите начальную дату формата 'год-месяц-число': ");
        String startDate = scanner.nextLine().trim();
        System.out.println("Введите конечную дату формата 'год-месяц-число': ");
        String endDate = scanner.nextLine().trim();
        try {
            System.out.println("Количество заработанной суммы за данный период: " + reportController.getProfitAtPeriod(startDate, endDate));
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный формат даты. Используйте формат: год-месяц-число.");
        }

    }
}

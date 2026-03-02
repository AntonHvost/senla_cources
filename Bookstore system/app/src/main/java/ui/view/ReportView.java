package ui.view;

import dto.OrderSummary;
import enums.SortByOrder;
import org.springframework.stereotype.Component;
import ui.controller.ReportController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class ReportView {

    private static final Logger logger = LoggerFactory.getLogger(ReportView.class);

    private final ReportController reportController;

    private final Scanner scanner = new Scanner(System.in);

    public ReportView(ReportController reportController) {
        this.reportController = reportController;
    }

    public void showCompletedOrderAtPeriodMenu() {
        logger.info("Request completed orders report for period");
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

        String choice = scanner.nextLine().trim();
        logger.debug("Report period: {} – {}, sorting: {}", startDate, endDate, choice);

        try {
            switch (choice) {
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
            logger.warn("Invalid date format in report request: {} – {}", startDate, endDate);
            System.out.println("Неверный формат даты. Используйте формат: год-месяц-число.");
            return;
        }

        if (orderList.isEmpty()) {
            logger.info("No completed orders found for period {} – {}", startDate, endDate);
            System.out.println("Нет завершенных заказов!\n");
        } else {
            logger.info("Found {} completed orders in period", orderList.size());
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
        logger.info("Request count of completed orders for period");

        System.out.println("Введите начальную дату формата 'год-месяц-число':");
        String startDate = scanner.nextLine().trim();
        System.out.println("Введите конечную дату формата 'год-месяц-число':");
        String endDate = scanner.nextLine().trim();

        try {
            System.out.println("Количество завершённых заказов за выбранный период: " + reportController.getCountCompletedOrdersAtPeriod(startDate, endDate));
            logger.info("Report completed!");
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid date format in count report: {} – {}", startDate, endDate);
            System.out.println("Неверный формат даты. Используйте формат: год-месяц-число.");
        }
    }

    public void showProfitAtPeriodMenu() {
        logger.info("Request profit report for period");
        System.out.println("Введите начальную дату формата 'год-месяц-число': ");
        String startDate = scanner.nextLine().trim();
        System.out.println("Введите конечную дату формата 'год-месяц-число': ");
        String endDate = scanner.nextLine().trim();

        try {
            BigDecimal profit = reportController.getProfitAtPeriod(startDate, endDate);
            System.out.println("Количество заработанной суммы за данный период: " + profit);
            logger.info("Report: profit = {} for period {} – {}", profit, startDate, endDate);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid date format in profit report: {} – {}", startDate, endDate);
            System.out.println("Неверный формат даты. Используйте формат: год-месяц-число.");
        }

    }
}

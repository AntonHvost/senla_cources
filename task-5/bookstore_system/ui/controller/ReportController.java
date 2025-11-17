package bookstore_system.ui.controller;

import bookstore_system.dto.OrderSummary;
import bookstore_system.enums.SortByOrder;
import bookstore_system.facade.OrderFacade;
import bookstore_system.facade.ReportFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReportController {

    private final ReportFacade reportFacade;
    private final Scanner scanner = new Scanner(System.in);

    public ReportController(ReportFacade reportFacade) {
        this.reportFacade = reportFacade;
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

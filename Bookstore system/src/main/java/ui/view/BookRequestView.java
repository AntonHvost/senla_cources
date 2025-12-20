package ui.view;

import di.annotation.Component;
import di.annotation.Inject;
import dto.BookRequestSummary;
import enums.SortByRequestBook;
import ui.controller.BookRequestController;

import java.util.*;

@Component
public class BookRequestView {

    private final BookRequestController bookRequestController;

    private final Scanner scanner = new Scanner(System.in);
    @Inject
    public BookRequestView(BookRequestController bookRequestController) {
        this.bookRequestController = bookRequestController;
    }

    public void showRestockBookMenu() {
        System.out.println("Введите ID книги, которую необходимо добавить на склад: ");
        bookRequestController.restockBook(scanner.nextLong());
        scanner.nextLine();
        System.out.println("Книга успешно добавлена на склад!");
    }

    public void showRequestsMenu() {
        List<BookRequestSummary> requestList = new ArrayList<>();
        System.out.println("Выберите желаемую сортировку:");
        System.out.println(
                "1. Сортировка по количеству запросов;" + "\n"
                        + "2. Сортировка по алфавиту.");

        switch (scanner.nextInt()) {
            case 1 -> {
                requestList = bookRequestController.getSortedRequests(SortByRequestBook.COUNT_REQUEST);
                break;
            }
            case 2 -> {
                requestList = bookRequestController.getSortedRequests(SortByRequestBook.ALPHABET);
                break;
            }
        }

        if (requestList.isEmpty()) {
            System.out.println("Список запросов пуст.\n");
            return;
        }

        System.out.println("=Детали запроса=\n");

        requestList.stream().forEach(request -> {
            System.out.println("Запрашиваемая книга: " + request.getBook().getTitle());
            System.out.println("Количество запросов на данную книгу: " + request.getRequestCount());
            System.out.println("==Данные о запросах==\n");
            request.getRequests().stream().forEach(curRequest -> {
                System.out.println("Номер запроса: " + curRequest.getId());
                System.out.println("Дата создания запроса: " + curRequest.getRequestDate());
                System.out.println("Дата поступления книги: " + curRequest.getDeliveryDate());
                System.out.println("Номер прикреплённого заказа: " + (curRequest.getRelatedOrderId() != null ? curRequest.getRelatedOrderId() : "Нет"));
                System.out.println("Статус заказа: " + curRequest.getStatus());
            });
        });
    }

    public void showImportBookRequestMenu() {
        System.out.println("Введите название файла: ");
        System.out.println("Рабочая папка: " + System.getProperty("user.dir"));
        String fileName = scanner.nextLine().trim();
        try {
            bookRequestController.importBookRequest(fileName + ".csv");
            System.out.println("Импорт запросов книг успешно завершён.");
        } catch (RuntimeException e) {
            System.out.println("Ошибка импорта из файла " + fileName + ". Проверьте экспортируемые данные или название файла на корректность.\n");
        }
    }

    public void showExportBookRequestMenu() {
        System.out.println("Введите название экспортируемого файла: ");
        String fileName = scanner.nextLine().trim();
        try {
            bookRequestController.exportBookRequest(fileName + ".csv");
            System.out.println("Запросы книг успешно экспортированы!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка экспорта в " + fileName + ". Проверьте название файла на корректность.\n");
        }

    }
}

package ui.view;

import di.annotation.Component;
import di.annotation.Inject;
import dto.BookRequestSummary;
import enums.SortByRequestBook;
import ui.controller.BookRequestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@Component
public class BookRequestView {

    private static final Logger logger = LoggerFactory.getLogger(BookRequestView.class);

    private final BookRequestController bookRequestController;

    private final Scanner scanner = new Scanner(System.in);
    @Inject
    public BookRequestView(BookRequestController bookRequestController) {
        this.bookRequestController = bookRequestController;
    }

    public void showRestockBookMenu() {
        logger.info("Initiated restocking of a book");
        System.out.println("Введите ID книги, которую необходимо добавить на склад: ");
        bookRequestController.restockBook(scanner.nextLong());
        scanner.nextLine();
        System.out.println("Книга успешно добавлена на склад!");
    }

    public void showRequestsMenu() {
        logger.info("Request list of book requests");
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
            logger.warn("List of requests is empty");
            System.out.println("Список запросов пуст.\n");
            return;
        }

        System.out.println("=Детали запроса=\n");
        logger.info("Displayed {} book requests", requestList.size());

        requestList.stream().forEach(request -> {
            System.out.println("Запрашиваемая книга №" + request.getBook().getId() + ": " + request.getBook().getTitle());
            System.out.println("Количество запросов на данную книгу: " + request.getRequestCount() + "\n");
            System.out.println("==Данные о запросах==\n");
            request.getRequests().stream().forEach(curRequest -> {
                System.out.println("Номер запроса: " + curRequest.getId());
                System.out.println("Дата создания запроса: " + curRequest.getRequestDate());
                System.out.println("Дата поступления книги: " + curRequest.getDeliveryDate());
                System.out.println("Номер прикреплённого заказа: " + (curRequest.getRelatedOrder().getId() != null ? curRequest.getRelatedOrder().getId() : "Нет"));
                System.out.println("Статус заказа: " + curRequest.getStatus() + "\n");
            });
        });
    }

    public void showImportBookRequestMenu() {
        logger.info("Initiated import of book requests");
        System.out.println("Введите название файла: ");
        System.out.println("Рабочая папка: " + System.getProperty("user.dir"));
        String fileName = scanner.nextLine().trim();
        logger.debug("Importing book requests from: {}.csv", fileName);
        try {
            bookRequestController.importBookRequest(fileName + ".csv");
            System.out.println("Импорт запросов книг успешно завершён.");
            logger.info("Book requests successfully imported from '{}.csv'", fileName);
        } catch (RuntimeException e) {
            System.out.println("Ошибка импорта из файла " + fileName + ". Проверьте экспортируемые данные или название файла на корректность.\n");
            logger.error("Failed to import book requests from '{}.csv'", fileName, e);
        }
    }

    public void showExportBookRequestMenu() {
        logger.info("Initiated export of book requests");
        System.out.println("Введите название экспортируемого файла: ");
        String fileName = scanner.nextLine().trim();
        logger.debug("Exporting book requests to: {}.csv", fileName);
        try {
            bookRequestController.exportBookRequest(fileName + ".csv");
            System.out.println("Запросы книг успешно экспортированы!");
            logger.info("Book requests successfully exported to '{}.csv'", fileName);
        } catch (RuntimeException e) {
            System.out.println("Ошибка экспорта в " + fileName + ". Проверьте название файла на корректность.\n");
            logger.error("Failed to export book requests to '{}.csv'", fileName, e);
        }

    }
}

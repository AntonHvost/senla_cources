package bookstore_system.ui.view;

import bookstore_system.domain.model.BookRequest;
import bookstore_system.dto.BookRequestSummary;
import bookstore_system.enums.SortByRequestBook;
import bookstore_system.ui.controller.BookRequestController;

import java.util.*;

public class BookRequestView {

    private final BookRequestController bookRequestController;

    private final Scanner scanner = new Scanner(System.in);

    public BookRequestView(BookRequestController bookRequestController) {
        this.bookRequestController = bookRequestController;
    }

    public void showCreateRequestBookMenu() {
        System.out.println("=Создание запроса на книгу=\n");
        System.out.println("Введите ID книги: ");

        long bookId;

        try {
            bookId = scanner.nextLong();
            scanner.nextLine();
        }
        catch (InputMismatchException e) {
            System.out.println("Должно быть целое число!");
            scanner.nextLine();
            return;
        }

        Optional<BookRequest> result = bookRequestController.createRequestBook(bookId);
        if (result.isPresent()) {
            System.out.println("Запрос успешно создан!");
        } else {
            System.out.println("Ошибка: Книга с ID " + bookId + " не найдена. Проверьте корректность ID.");
        }
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

        System.out.println("=Детали запроса=\n");

        requestList.stream().forEach(request -> {
            System.out.println("Запрашиваемая книга: " + request.getBook().getTitle());
            System.out.println("Количество запросов на данную книгу: " + request.getRequestCount());
            System.out.println("==Данные о запросах==\n");
            request.getRequests().stream().forEach(curRequest -> {
                System.out.println("Номер запроса: " + curRequest.getId());
                System.out.println("Дата создания запроса: " + curRequest.getRequestDate());
                System.out.println("Дата поступления книги: " + curRequest.getDeliveryDate());
                System.out.println("Номер прикреплённого заказа: " + (curRequest.getRelatedOrder() != null ? curRequest.getRelatedOrder() : "Нет"));
                System.out.println("Статус заказа: " + curRequest.getStatus());
            });
        });
    }

    public void showImportBookRequestMenu() {
        System.out.println("Введите название файла: ");
        System.out.println("Рабочая папка: " + System.getProperty("user.dir"));
        bookRequestController.importBookRequest(scanner.nextLine().trim());
    }

    public void showExportBookRequestMenu() {
        System.out.println("Введите название экспортируемого файла: ");
        bookRequestController.exportBookRequest(scanner.nextLine().trim() + ".csv");
        System.out.println("Книги успешно экспортированы!");
    }
}

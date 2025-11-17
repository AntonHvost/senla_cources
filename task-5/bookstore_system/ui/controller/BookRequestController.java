package bookstore_system.ui.controller;

import bookstore_system.domain.BookRequest;
import bookstore_system.dto.BookRequestSummary;
import bookstore_system.enums.SortByRequestBook;
import bookstore_system.facade.ReportFacade;
import bookstore_system.facade.RequestFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookRequestController {
    private final RequestFacade requestFacade;
    private final ReportFacade reportFacade;
    private final Scanner scanner = new Scanner(System.in);

    public BookRequestController(RequestFacade requestFacade, ReportFacade reportFacade) {
        this.requestFacade = requestFacade;
        this.reportFacade = reportFacade;
    }

    public void requestBook() {
        System.out.println("=Создание запроса на книгу=\n");
        System.out.println("Введите ID книги: ");
        requestFacade.requestBook(scanner.nextLong());
        System.out.println("Запрос успешно создан!");
    }

    public void restockBook() {
        System.out.println("Введите ID книги, которую необходимо добавить на склад: ");
        requestFacade.restockBook(scanner.nextLong());
        System.out.println("Книга успешно добавлена на склад!");
    }

    public void showRequestList() {
        List<BookRequestSummary> requestList = new ArrayList<>();
        System.out.println("Выберите желаемую сортировку:");
        System.out.println(
                "1. Сортировка по количеству запросов;" + "\n"
            + "2. Сортировка по алфавиту.");

        switch (scanner.nextInt()) {
            case 1 -> {
               requestList = reportFacade.getRequestList(SortByRequestBook.COUNT_REQUEST);
                break;
            }
            case 2 -> {
                requestList = reportFacade.getRequestList(SortByRequestBook.ALPHABET);
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
                System.out.println("Номер прикреплённого заказа: " + (curRequest.getRelatedOrder() != null ? curRequest.getRelatedOrder().getId() : "Нет"));
                System.out.println("Статус заказа: " + curRequest.getStatus());
            });
        });

    }

}

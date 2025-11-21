package bookstore_system.ui.controller;

import bookstore_system.dto.BookSummary;
import bookstore_system.enums.BookStatus;
import bookstore_system.enums.SortByBook;
import bookstore_system.enums.SortByUnsoldBook;
import bookstore_system.facade.BookFacade;
import bookstore_system.facade.ReportFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookController {
    private final BookFacade bookFacade;
    private final ReportFacade reportFacade;
    private final Scanner scanner = new Scanner(System.in);

    public BookController(BookFacade bookFacade, ReportFacade reportFacade) {
        this.bookFacade = bookFacade;
        this.reportFacade = reportFacade;
    }

    public void showBooks() {
        List<BookSummary> info = new ArrayList<>();
        System.out.println("Выберите параметр сортировки:\n" +
                "1. По алфавиту;\n" +
                "2. По дате публикации;\n" +
                "3. По цене;\n" +
                "4. По статусу книгии.\n");

        switch(scanner.nextLine().trim()) {
            case "1" -> {
                info = reportFacade.getBookCatalog(SortByBook.ALPHABET);
                break;
            }
            case "2" -> {
                info = reportFacade.getBookCatalog(SortByBook.PUBLICATION_DATE);
                break;
            }
            case "3" -> {
            info = reportFacade.getBookCatalog(SortByBook.PRICE);
            break;
            }

            case "4" -> {
            info = reportFacade.getBookCatalog(SortByBook.IN_STOCK);
            break;
            }
            default -> {
                System.out.println("Неверный параметр сортировки");
            }

        }

        System.out.println("\nСписок книг:");
        info.forEach(bookSummary -> System.out.println(
                "Название: "  + bookSummary.getTitle() + "\n"
                + "Автор: "  + bookSummary.getAuthor() + "\n"
                + "Описание: " + bookSummary.getDescription() + "\n"
                + "Дата публикации: " + bookSummary.getPrice() + "\n"
                + "Цена: "  + bookSummary.getPrice() + "\n"
                + "Статус: " + (bookSummary.getStatus() == BookStatus.AVAILABLE ? "В наличии" : "Отсутствует") + "\n"
        ));
    }

    public void showUnsoldBooks() {
        List<BookSummary> info = new ArrayList<>();
        System.out.println("Выберите параметр сортировки:\n" +
                "1. По дате доставки;\n" +
                "2. По цене;\n");

        switch(scanner.nextLine().trim()) {
            case "1" -> {
                info = reportFacade.getUnsoldBooks(SortByUnsoldBook.DELIVERY_DATE);
                break;
            }
            case "2" -> {
                info = reportFacade.getUnsoldBooks(SortByUnsoldBook.PRICE);
                break;
            }
            default -> {
                System.out.println("Неверный параметр сортировки");
            }

        }
        System.out.println("\nСписок непроданных книг за срок не более 6 месяцев:");
        info.forEach(bookSummary -> System.out.println(
                "Название: "  + bookSummary.getTitle() + "\n"
                        + "Автор: "  + bookSummary.getAuthor() + "\n"
                        + "Последняя цена поставки: " + bookSummary.getDeliveryDate() + "\n"
                        + "Цена: "  + bookSummary.getPrice() + "\n"
                        + "Статус: " + (bookSummary.getStatus() == BookStatus.AVAILABLE ? "В наличии" : "Отсутствует") + "\n"
        ));

    }

    public void showBookDescription () {
        System.out.println("Выберите книгу из представленного списка: ");
        reportFacade.getBookCatalog(SortByBook.ALPHABET).stream().forEach(book -> System.out.println(book.getTitle()));
        String description = reportFacade.getBookDescription(scanner.nextInt());
        System.out.println(description);
    }
}

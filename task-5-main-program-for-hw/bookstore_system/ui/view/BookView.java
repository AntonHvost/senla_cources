package bookstore_system.ui.view;

import bookstore_system.dto.BookSummary;
import bookstore_system.enums.BookStatus;
import bookstore_system.enums.SortByBook;
import bookstore_system.enums.SortByUnsoldBook;
import bookstore_system.facade.BookFacade;
import bookstore_system.facade.ReportFacade;
import bookstore_system.ui.controller.BookController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookView {

    private final BookController bookController;

    private final Scanner scanner = new Scanner(System.in);

    public BookView(BookController bookController) {
        this.bookController = bookController;
    }

    public void showBooksMenu() {
        List<BookSummary> info = new ArrayList<>();
        System.out.println("Выберите параметр сортировки:\n" +
                "1. По алфавиту;\n" +
                "2. По дате публикации;\n" +
                "3. По цене;\n" +
                "4. По статусу книгии.\n");

        switch(scanner.nextLine().trim()) {
            case "1" -> {
                info = bookController.getSortedBooks(SortByBook.ALPHABET);
                break;
            }
            case "2" -> {
                info = bookController.getSortedBooks(SortByBook.PUBLICATION_DATE);
                break;
            }
            case "3" -> {
                info = bookController.getSortedBooks(SortByBook.PRICE);
                break;
            }

            case "4" -> {
                info = bookController.getSortedBooks(SortByBook.IN_STOCK);
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
                        + "Дата публикации: " + bookSummary.getPublishDate() + "\n"
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
                info = bookController.getSortedUnsoldBooks(SortByUnsoldBook.DELIVERY_DATE);
                break;
            }
            case "2" -> {
                info = bookController.getSortedUnsoldBooks(SortByUnsoldBook.PRICE);
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
                        + "Последняя цена поставки: " + (bookSummary.getDeliveryDate() == null ? bookSummary.getDeliveryDate() : "отсутствует") + "\n"
                        + "Цена: "  + bookSummary.getPrice() + "\n"
                        + "Статус: " + (bookSummary.getStatus() == BookStatus.AVAILABLE ? "В наличии" : "Отсутствует") + "\n"
        ));

    }

    public void showBookDescription () {
        System.out.println("Выберите книгу из представленного списка: ");
        bookController.getSortedBooks(SortByBook.ALPHABET).stream().forEach(book -> System.out.println(book.getTitle()));
        String description = bookController.getBookDescription(scanner.nextInt());
        System.out.println(description);
    }

    public void showBookImportMenu () {
        System.out.println("Введите название файла: ");
        System.out.println("Рабочая папка: " + System.getProperty("user.dir"));
        bookController.importBooks(scanner.nextLine().trim());
    }

    public void showBookExportMenu () {
        System.out.println("Введите название экспортируемого файла: ");
        bookController.exportBooks(scanner.nextLine().trim() + ".csv");
        System.out.println("Книги успешно экспортированы!");
    }
}

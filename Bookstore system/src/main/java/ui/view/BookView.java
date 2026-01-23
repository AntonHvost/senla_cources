package ui.view;

import di.annotation.Component;
import di.annotation.Inject;
import dto.BookSummary;
import enums.BookStatus;
import enums.SortByBook;
import enums.SortByUnsoldBook;
import ui.controller.BookController;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
public class BookView {

    private final BookController bookController;

    private final Scanner scanner = new Scanner(System.in);

    @Inject
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
                        + "Последняя дата поставки: " + (bookSummary.getDeliveryDate() != null ? bookSummary.getDeliveryDate() : "отсутствует") + "\n"
                        + "Цена: "  + bookSummary.getPrice() + "\n"
                        + "Статус: " + (bookSummary.getStatus() == BookStatus.AVAILABLE ? "В наличии" : "Отсутствует") + "\n"
        ));

    }

    public void showBookDescription () {
        System.out.println("Выберите книгу из представленного списка: ");

        bookController.getSortedBooks(SortByBook.ID).stream().forEach(book -> System.out.println(book.getId() + ". " + book.getTitle()));

        Long id = Long.parseLong(scanner.nextLine().trim());

        try {
            String description = bookController.getBookDescription(id);
            System.out.println(description);
        } catch (InputMismatchException e) {
            System.out.println("Введён неверный формат числа.");
        }
    }

    public void showBookImportMenu () {
        System.out.println("Введите название файла: ");
        System.out.println("Рабочая папка: " + System.getProperty("user.dir"));
        String fileName = scanner.nextLine().trim();
        try {
            bookController.importBooks(fileName + ".csv");
            System.out.println("Импорт книг успешно завершён.");
        } catch (RuntimeException e) {
            System.out.println("Ошибка импорта из файла " + fileName + ". Проверьте экспортируемые данные или название файла на корректность.\n");
        }
    }

    public void showBookExportMenu () {
        System.out.println("Введите название экспортируемого файла: ");
        String fileName = scanner.nextLine().trim();
        try {
            bookController.exportBooks(fileName + ".csv");
            System.out.println("Книги успешно экспортированы!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка экспорта в " + fileName + ". Проверьте название файла на корректность.\n");
        }

    }
}

package ui.view;

import dto.BookSummary;
import enums.BookStatus;
import enums.SortByBook;
import enums.SortByUnsoldBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ui.controller.BookController;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
public class BookView {

    private static final Logger logger = LoggerFactory.getLogger(BookView.class);

    private final BookController bookController;
    private final Scanner scanner = new Scanner(System.in);
    @Value("${unsoldBookMonth}")
    private String unsoldBookMonth;

    public BookView(BookController bookController) {
        this.bookController = bookController;
    }

    public void showBooksMenu() {
        logger.info("Opened book list menu");
        List<BookSummary> info = new ArrayList<>();
        System.out.println("Выберите параметр сортировки:\n" +
                "1. По алфавиту;\n" +
                "2. По дате публикации;\n" +
                "3. По цене;\n" +
                "4. По статусу книгии.\n");

        String choice = scanner.nextLine().trim();
        logger.debug("Sorting option selected: {}", choice);

        switch(choice) {
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
                logger.warn("Invalid sorting option entered: {}", choice);
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
        logger.info("Requesting unsold books...");
        System.out.println("Примечание: непроданные книги будут отображаться за период не более " + unsoldBookMonth + " месяцев.");
        List<BookSummary> info = new ArrayList<>();
        System.out.println("Выберите параметр сортировки:\n" +
                "1. По дате доставки;\n" +
                "2. По цене;\n");

        switch(scanner.nextLine().trim()) {
            case "1" -> {
                info = bookController.getSortedUnsoldBooks(SortByUnsoldBook.DELIVERY_DATE);
                logger.info("Sorting option selected: {}", SortByUnsoldBook.DELIVERY_DATE.name());
                break;
            }
            case "2" -> {
                info = bookController.getSortedUnsoldBooks(SortByUnsoldBook.PRICE);
                logger.info("Sorting option selected: {}", SortByUnsoldBook.PRICE.name());
                break;
            }
            default -> {
                System.out.println("Неверный параметр сортировки");
                logger.warn("Invalid sorting option entered: {}", scanner.nextLine());
            }

        }
        System.out.println("\nСписок непроданных книг за срок не более " + unsoldBookMonth + " месяцев:");
        logger.info("Displayed {} books", info.size());
        info.forEach(bookSummary -> System.out.println(
                "Название: "  + bookSummary.getTitle() + "\n"
                        + "Автор: "  + bookSummary.getAuthor() + "\n"
                        + "Последняя дата поставки: " + (bookSummary.getDeliveryDate() != null ? bookSummary.getDeliveryDate() : "отсутствует") + "\n"
                        + "Цена: "  + bookSummary.getPrice() + "\n"
                        + "Статус: " + (bookSummary.getStatus() == BookStatus.AVAILABLE ? "В наличии" : "Отсутствует") + "\n"
        ));

    }

    public void showBookDescription () {
        logger.info("Requesting book description...");
        System.out.println("Выберите книгу из представленного списка: ");

        bookController.getSortedBooks(SortByBook.ID).stream().forEach(book -> System.out.println(book.getId() + ". " + book.getTitle()));

        Long id = Long.parseLong(scanner.nextLine().trim());

        try {
            logger.info("Showing book description");
            String description = bookController.getBookDescription(id);
            System.out.println(description);
        } catch (InputMismatchException e) {
            logger.warn("Invalid input entered: {}", e.getMessage());
            System.out.println("Введён неверный формат числа.");
        }
    }

    public void showBookImportMenu () {
        logger.info("Initiated book import");
        System.out.println("Введите название файла: ");
        System.out.println("Рабочая папка: " + System.getProperty("user.dir"));
        String fileName = scanner.nextLine().trim();
        logger.debug("Importing books from file: {}.csv", fileName);
        try {
            bookController.importBooks(fileName + ".csv");
            System.out.println("Импорт книг успешно завершён.");
            logger.info("Books successfully imported from '{}.csv'", fileName);
        } catch (RuntimeException e) {
            System.out.println("Ошибка импорта из файла " + fileName + ". Проверьте экспортируемые данные или название файла на корректность.\n");
            logger.error("Failed to import books from '{}.csv'", fileName, e);
        }
    }

    public void showBookExportMenu () {
        logger.info("Initiated book export");
        System.out.println("Введите название экспортируемого файла: ");
        String fileName = scanner.nextLine().trim();
        logger.debug("Exporting books to file: {}.csv", fileName);
        try {
            bookController.exportBooks(fileName + ".csv");
            System.out.println("Книги успешно экспортированы!");
            logger.info("Books successfully exported to '{}.csv'", fileName);
        } catch (RuntimeException e) {
            System.out.println("Ошибка экспорта в " + fileName + ". Проверьте название файла на корректность.\n");
            logger.error("Failed to export books to '{}.csv'", fileName, e);
        }

    }
}

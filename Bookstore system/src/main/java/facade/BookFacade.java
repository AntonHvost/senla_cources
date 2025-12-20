package facade;

import di.annotation.Component;
import di.annotation.Inject;
import domain.model.Book;
import domain.service.BookInventoryService;
import domain.service.IOService;
import io.csv.converter.BookCsvConverter;

@Component
public class BookFacade {
    private final BookInventoryService bookInventoryService;
    private final IOService ioService;
    private final BookCsvConverter bookCSVConverter = new BookCsvConverter();

    @Inject
    public BookFacade(BookInventoryService bookInventoryService, IOService ioService) {
        this.bookInventoryService = bookInventoryService;
        this.ioService = ioService;
    }

    public void addBookToCatalog(Book book) {
        bookInventoryService.addBookToCatalog(book);
    }

    public boolean isBookAvailable(long bookId) {
        return bookInventoryService.isAvailable(bookId);
    }

    public void importBooksFromCSV(String filename) {
        ioService.importEntities(filename, bookInventoryService::findBookById, bookInventoryService::saveBook, bookInventoryService::updateBook, bookCSVConverter);
    }

    public void exportBooksToCSV(String filename) {
        ioService.exportEntities(filename, bookInventoryService::getBooks, bookCSVConverter);
    }

}

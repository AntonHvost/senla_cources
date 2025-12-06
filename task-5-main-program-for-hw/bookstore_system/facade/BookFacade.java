package bookstore_system.facade;

import bookstore_system.domain.model.Book;
import bookstore_system.domain.service.BookInventoryService;
import bookstore_system.domain.service.IOService;
import bookstore_system.io.csv.converter.BookCsvConverter;

public class BookFacade {

    private final BookInventoryService bookInventoryService;
    private final IOService ioService;
    private final BookCsvConverter bookCSVConverter = new BookCsvConverter();

    public BookFacade(BookInventoryService bookInventoryService, IOService ioService) {
        this.bookInventoryService = bookInventoryService;
        this.ioService = ioService;
    }

    public void addBookToCatalog(Book book) {
        bookInventoryService.addBookToCatalog(book);
    }

    public boolean isBookAvailable(long bookId) {
        return bookInventoryService.findBookById(bookId)
                .map(Book::isAvaible)
                .orElse(false);
    }

    public void importBooksFromCSV(String filename) {
        ioService.importEntities(filename, bookInventoryService::findBookById, bookInventoryService::saveBook, bookInventoryService::updateBook, bookCSVConverter);
    }

    public void exportBooksToCSV(String filename) {
        ioService.exportEntities(filename, bookInventoryService::getBooks, bookCSVConverter);
    }

}

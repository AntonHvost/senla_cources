package bookstore_system.facade;

import bookstore_system.domain.Book;
import bookstore_system.service.BookInventoryService;

public class BookFacade {

    private final BookInventoryService bookInventoryService;

    public BookFacade(BookInventoryService bookInventoryService) {
        this.bookInventoryService = bookInventoryService;
    }

    public void addBookToCatalog(Book book) {
        bookInventoryService.addBookToCatalog(book);
    }

    public boolean isBookAvailable(long bookId) {
        return bookInventoryService.findBookById(bookId)
                .map(Book::isAvaible)
                .orElse(false);
    }
}

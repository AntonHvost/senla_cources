package bookstore_system.facade;

import bookstore_system.domain.Book;
import bookstore_system.domain.BookCatalog;

public class BookFacade {

    private final BookCatalog bookCatalog;

    public BookFacade(BookCatalog bookCatalog) {
        this.bookCatalog = bookCatalog;
    }

    public void addBookToCatalog(Book book) {
        bookCatalog.addBookToCatalog(book);
    }

    public boolean isBookAvailable(long bookId) {
        return bookCatalog.findBookById(bookId)
                .map(Book::isAvaible)
                .orElse(false);
    }
}

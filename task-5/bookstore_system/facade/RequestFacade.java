package bookstore_system.facade;

import bookstore_system.domain.Book;
import bookstore_system.domain.BookCatalog;
import bookstore_system.domain.BookRequest;
import bookstore_system.service.RequestService;

public class RequestFacade {

    private RequestService requestService;
    private BookCatalog bookCatalog;

    public BookRequest requestBook(long bookId) {
        Book book = bookCatalog.findBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Книга с ID " + bookId + " не найдена"));
        return requestService.createRequest(book, null);
    }

    public void restockBook(long bookId){
        bookCatalog.restockBook(bookId);
        requestService.fulfillRequests(bookId);
    }
}

package bookstore_system.facade;

import bookstore_system.domain.Book;
import bookstore_system.service.BookInventoryService;
import bookstore_system.domain.BookRequest;
import bookstore_system.service.RequestService;

import java.util.Optional;

public class RequestFacade {

    private final RequestService requestService;
    private final BookInventoryService bookInventoryService;

    public RequestFacade(RequestService requestService, BookInventoryService bookInventoryService) {
        this.requestService = requestService;
        this.bookInventoryService = bookInventoryService;
    }

    public Optional<BookRequest> requestBook(long bookId) {
        return bookInventoryService.findBookById(bookId)
                .map(book -> requestService.createRequest(book, null));
    }

    public void restockBook(long bookId){
        bookInventoryService.restockBook(bookId);
        requestService.fulfillRequests(bookId);
    }
}

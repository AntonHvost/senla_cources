package bookstore_system.facade;

import bookstore_system.domain.service.*;
import bookstore_system.domain.model.BookRequest;
import bookstore_system.io.csv.converter.BookRequestCSVConverter;

import java.util.Optional;

public class RequestFacade {

    private final RequestService requestService;
    private final BookInventoryService bookInventoryService;
    private final BookRequestFullfilmentService bookRequestFullfilmentService;
    private final IOService ioService;
    private final BookRequestCSVConverter bookRequestCSVConverter = new BookRequestCSVConverter();

    public RequestFacade(RequestService requestService, BookInventoryService bookInventoryService, BookRequestFullfilmentService bookRequestFullfilmentService, IOService ioService) {
        this.requestService = requestService;
        this.bookInventoryService = bookInventoryService;
        this.bookRequestFullfilmentService = bookRequestFullfilmentService;
        this.ioService = ioService;
    }

    public Optional<BookRequest> requestBook(Long bookId) {
       return Optional.of(requestService.createRequest(bookId, null));
    }

    public void restockBook (Long bookId) {
        bookInventoryService.restockBook(bookId);
        bookRequestFullfilmentService.fulfillRequests(bookId);

    }

    public void importBookRequest (String filename) {
        ioService.importEntities(filename, requestService::findRequestById, requestService::save, requestService::update, bookRequestCSVConverter);
    }

    public void exportBookRequest (String filename) {
        ioService.exportEntities(filename, requestService::getRequestsList, bookRequestCSVConverter);
    }

}

package facade;

import di.annotation.Component;
import di.annotation.Inject;
import domain.model.BookRequest;
import io.csv.converter.BookRequestCSVConverter;

import service.BookInventoryService;
import service.BookRequestFullfilmentService;
import service.IOService;
import service.RequestService;

import java.util.Optional;

@Component
public class RequestFacade {

    private final RequestService requestService;
    private final BookInventoryService bookInventoryService;
    private final BookRequestFullfilmentService bookRequestFullfilmentService;
    private final IOService ioService;
    private final BookRequestCSVConverter bookRequestCSVConverter = new BookRequestCSVConverter();

    @Inject
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

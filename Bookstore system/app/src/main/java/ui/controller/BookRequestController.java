package ui.controller;

import di.annotation.Component;
import di.annotation.Inject;
import domain.model.BookRequest;
import dto.BookRequestSummary;
import enums.SortByRequestBook;
import facade.ReportFacade;
import facade.RequestFacade;

import java.util.List;
import java.util.Optional;

@Component
public class BookRequestController {
    private final RequestFacade requestFacade;
    private final ReportFacade reportFacade;
    @Inject
    public BookRequestController(RequestFacade requestFacade, ReportFacade reportFacade) {
        this.requestFacade = requestFacade;
        this.reportFacade = reportFacade;
    }

    public Optional<BookRequest> createRequestBook(Long requestId) {
       return requestFacade.requestBook(requestId);
    }

    public void restockBook(Long bookId) {
        requestFacade.restockBook(bookId);
    }

    public List<BookRequestSummary> getSortedRequests(SortByRequestBook sortByRequestBook) {
        return reportFacade.getRequestList(sortByRequestBook);
    }

    public void importBookRequest (String filename) {
        requestFacade.importBookRequest(filename);
    }

    public void exportBookRequest (String filename) {
        requestFacade.exportBookRequest(filename);
    }

}

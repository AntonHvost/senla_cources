package ui.controller;

import domain.model.impl.Book;
import domain.model.impl.BookRequest;
import dto.BookRequestSummary;
import enums.SortByRequestBook;
import facade.ReportFacade;
import facade.RequestFacade;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class BookRequestController {
    private final RequestFacade requestFacade;
    private final ReportFacade reportFacade;

    public BookRequestController(RequestFacade requestFacade, ReportFacade reportFacade) {
        this.requestFacade = requestFacade;
        this.reportFacade = reportFacade;
    }

    public Optional<BookRequest> createRequestBook(Book requestId) {
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

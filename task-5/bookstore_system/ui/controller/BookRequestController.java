package bookstore_system.ui.controller;

import bookstore_system.domain.BookRequest;
import bookstore_system.dto.BookRequestSummary;
import bookstore_system.enums.SortByRequestBook;
import bookstore_system.facade.ReportFacade;
import bookstore_system.facade.RequestFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookRequestController {
    private final RequestFacade requestFacade;
    private final ReportFacade reportFacade;

    public BookRequestController(RequestFacade requestFacade, ReportFacade reportFacade) {
        this.requestFacade = requestFacade;
        this.reportFacade = reportFacade;
    }

    public void createRequestBook(long requestId) {
        requestFacade.requestBook(requestId);
    }

    public void restockBook(long bookId) {
        requestFacade.restockBook(bookId);
    }

    public List<BookRequestSummary> getSortedRequests(SortByRequestBook sortByRequestBook) {
        return reportFacade.getRequestList(sortByRequestBook);
    }

}

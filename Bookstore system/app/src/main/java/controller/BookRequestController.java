package controller;

import domain.model.impl.Book;
import domain.model.impl.BookRequest;
import dto.BookRequestSummary;
import enums.SortByRequestBook;
import facade.ReportFacade;
import facade.RequestFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/book-request")
public class BookRequestController {
    private final RequestFacade requestFacade;
    private final ReportFacade reportFacade;

    public BookRequestController(RequestFacade requestFacade, ReportFacade reportFacade) {
        this.requestFacade = requestFacade;
        this.reportFacade = reportFacade;
    }

    /*@PostMapping
    public ResponseEntity<Optional<BookRequest>> createRequestBook(@PathVariable Book requestId) {
       return ResponseEntity.ok(requestFacade.requestBook(requestId));
    }*/

    @GetMapping("/restock/{id}")
    public ResponseEntity<String> restockBook(@PathVariable("id") Long bookId) {
        requestFacade.restockBook(bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Book request has been created");
    }

    @GetMapping
    public ResponseEntity<List<BookRequestSummary>> getSortedRequests(@RequestParam(value = "sortBy", required = false) SortByRequestBook sortByRequestBook) {
        return ResponseEntity.ok(reportFacade.getRequestList(sortByRequestBook));
    }

    public void importBookRequest (String filename) {
        requestFacade.importBookRequest(filename);
    }

    public void exportBookRequest (String filename) {
        requestFacade.exportBookRequest(filename);
    }

}

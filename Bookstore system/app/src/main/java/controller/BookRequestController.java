package controller;

import dto.response.BookRequestSummary;
import enums.SortByRequestBook;
import facade.ReportFacade;
import facade.RequestFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/book-requests")
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

    @PostMapping("/{id}/restock")
    public ResponseEntity<String> restockBook(@PathVariable("id") Long bookId) {
        boolean result = requestFacade.restockBook(bookId);
        if (result) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Book request has been restocked successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book request has been restocked badly");
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

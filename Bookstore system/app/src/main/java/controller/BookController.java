package controller;

import dto.BookSummary;
import enums.SortByBook;
import enums.SortByUnsoldBook;
import facade.BookFacade;
import facade.ReportFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookController {
    private final ReportFacade reportFacade;
    private final BookFacade bookFacade;

    public BookController(ReportFacade reportFacade, BookFacade bookFacade) {
        this.reportFacade = reportFacade;
        this.bookFacade = bookFacade;
    }

    @GetMapping
    public ResponseEntity<List<BookSummary>> getSortedBooks(@RequestParam(required = false) SortByBook sortByBook) {
        return ResponseEntity.ok (reportFacade.getBookCatalog(sortByBook));
    }

    @GetMapping("/unsold")
    public ResponseEntity<List<BookSummary>> getSortedUnsoldBooks(@RequestParam(required = false) SortByUnsoldBook sortByUnsoldBook) {
        return ResponseEntity.ok(reportFacade.getUnsoldBooks(sortByUnsoldBook));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getBookDescription(@PathVariable("id") Long bookId) {
        String description = reportFacade.getBookDescription(bookId);
        if (description == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(description);
    }

    public void importBooks(String filename) {
        bookFacade.importBooksFromCSV(filename);
    }

    public void exportBooks(String filename) {
        bookFacade.exportBooksToCSV(filename);
    }

}

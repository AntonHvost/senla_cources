package controller;

import dto.response.BookResponseDto;
import dto.response.BookSummary;
import dto.response.BookDescriptionResponseDto;
import enums.SortByBook;
import enums.SortByUnsoldBook;
import facade.BookFacade;
import facade.ReportFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<List<BookResponseDto>> getSortedBooks(@RequestParam(value = "sortByBook", required = false) SortByBook sortByBook) {
        return ResponseEntity.ok (reportFacade.getBookCatalog(sortByBook));
    }

    @GetMapping("/unsold")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookSummary>> getSortedUnsoldBooks(@RequestParam(value = "sortByUnsoldBook", required = false) SortByUnsoldBook sortByUnsoldBook) {
        return ResponseEntity.ok(reportFacade.getUnsoldBooks(sortByUnsoldBook));
    }

    @GetMapping("/get-description/{id}")
    public ResponseEntity<BookDescriptionResponseDto> getBookDescription(@PathVariable("id") Long bookId) {
        BookDescriptionResponseDto description = reportFacade.getBookDescription(bookId);
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

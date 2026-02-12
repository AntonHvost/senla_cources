package ui.controller;

import dto.BookSummary;
import enums.SortByBook;
import enums.SortByUnsoldBook;
import facade.BookFacade;
import facade.ReportFacade;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {
    private final ReportFacade reportFacade;
    private final BookFacade bookFacade;

    public BookController(ReportFacade reportFacade, BookFacade bookFacade) {
        this.reportFacade = reportFacade;
        this.bookFacade = bookFacade;
    }

    public List<BookSummary> getSortedBooks(SortByBook sortByBook) {
        return reportFacade.getBookCatalog(sortByBook);
    }

    public List<BookSummary> getSortedUnsoldBooks(SortByUnsoldBook sortByUnsoldBook) {
        return reportFacade.getUnsoldBooks(sortByUnsoldBook);
    }

    public String getBookDescription(Long bookId) {
        return reportFacade.getBookDescription(bookId);
    }

    public void importBooks(String filename) {
        bookFacade.importBooksFromCSV(filename);
    }

    public void exportBooks(String filename) {
        bookFacade.exportBooksToCSV(filename);
    }

}

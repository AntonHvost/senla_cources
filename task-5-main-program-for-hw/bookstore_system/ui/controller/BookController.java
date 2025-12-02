package bookstore_system.ui.controller;

import bookstore_system.dto.BookSummary;
import bookstore_system.enums.SortByBook;
import bookstore_system.enums.SortByUnsoldBook;
import bookstore_system.facade.BookFacade;
import bookstore_system.facade.ReportFacade;

import java.util.List;

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

    public String getBookDescription(int bookId) {
        return reportFacade.getBookDescription(bookId);
    }

    public void importBooks(String filename) {
        bookFacade.importBooksFromCSV(filename);
    }

    public void exportBooks(String filename) {
        bookFacade.exportBooksToCSV(filename);
    }

}

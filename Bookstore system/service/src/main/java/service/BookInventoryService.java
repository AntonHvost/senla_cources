package service;

import di.annotation.Component;
import di.annotation.Inject;
import domain.model.Book;
import repository.BookRepository;
import enums.BookStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Component
public class BookInventoryService {

    private static final Logger logger = LoggerFactory.getLogger(BookInventoryService.class);

    private final BookRepository bookRepository;

    @Inject
    public BookInventoryService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBookToCatalog(Book b){
        logger.info("Adding new book to catalog: {}", b.getTitle());
        bookRepository.save(b);
    }

    public Optional<Book> findBookById(long bookId){
        logger.debug("Fetching book by ID: {}", bookId);
        return bookRepository.findById(bookId);
    }

    public void restockBook(long bookId){
        logger.info("Restocking book ID: {}", bookId);
        bookRepository.findById(bookId).ifPresent(book -> {
            book.setStatus(BookStatus.AVAILABLE);
            bookRepository.update(book);
            logger.info("Book ID {} marked as AVAILABLE", bookId);
        });
    }

    public void saveBook(Book book){
        logger.debug("Saving book: ID={}, title={}", book.getId(), book.getTitle());
        bookRepository.save(book);
    }

    public void updateBook(Book book){
        logger.debug("Updating book: ID={}, title={}", book.getId(), book.getTitle());
        bookRepository.update(book);
    }

    public boolean isAvailable(Long bookId){
        logger.debug("Checking availability for book ID: {}", bookId);
        return bookRepository.findById(bookId).get().getStatus().equals(BookStatus.AVAILABLE);
    }

    public List<Book> getBooks() {
        logger.debug("Fetching all books from inventory");
        return bookRepository.findAll();
    }
}

package service;

import di.annotation.Component;
import di.annotation.Inject;
import domain.model.Book;
import repository.BookRepository;
import enums.BookStatus;

import java.util.List;
import java.util.Optional;

@Component
public class BookInventoryService {
    private final BookRepository bookRepository;

    @Inject
    public BookInventoryService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBookToCatalog(Book b){
        bookRepository.save(b);
    }

    public Optional<Book> findBookById(long bookId){
        return bookRepository.findById(bookId);
    }

    public void restockBook(long bookId){
        bookRepository.findById(bookId).ifPresent(book -> {
            book.setStatus(BookStatus.AVAILABLE);
            bookRepository.update(book);
        });
    }

    public void saveBook(Book book){
        bookRepository.save(book);
    }

    public void updateBook(Book book){
        bookRepository.update(book);
    }

    public boolean isAvailable(Long bookId){
        return bookRepository.findById(bookId).get().getStatus().equals(BookStatus.AVAILABLE);
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }
}

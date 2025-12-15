package bookstore_system.domain.service;

import bookstore_system.di.annotation.Component;
import bookstore_system.di.annotation.Inject;
import bookstore_system.domain.model.Book;
import bookstore_system.domain.repository.BookRepository;
import bookstore_system.enums.BookStatus;

import java.util.ArrayList;
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
        Long nextId = bookRepository.generateNextId();
        b.setId(nextId);
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
        if(book.getId() == null || book.getId() == 0){
            book.setId(bookRepository.generateNextId());
        }
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

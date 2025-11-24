package bookstore_system.service;

import bookstore_system.domain.Book;
import bookstore_system.enums.BookStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookInventoryService {
    private List<Book> books;

    public BookInventoryService() {
        this.books = new ArrayList<>();
    }

    public void addBookToCatalog(Book b){
        books.add(b);
    }

    public Optional<Book> findBookById(long bookId){
        return books.stream()
                .filter(b -> b.getId() == bookId)
                .findAny();
    }

    public void restockBook(long bookId){
        findBookById(bookId).ifPresent(book -> {book.setStatus(BookStatus.AVAILABLE);});
    }

    public List<Book> getBooks() {
        return books;
    }
}

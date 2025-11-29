package bookstore_system.domain.service;

import bookstore_system.domain.model.Book;
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

    public void saveBook(Book book){
        books.add(book);
    }

    public void updateBook(Book book){
        for (Book b : books){
            if(b.getId() == book.getId()){
                books.set(books.indexOf(b), book);
                return;
            }
        }
    }

    public List<Book> getBooks() {
        return books;
    }
}

package bookstore_system.domain.service;

import bookstore_system.di.annotation.Component;
import bookstore_system.di.annotation.Inject;
import bookstore_system.domain.model.Book;
import bookstore_system.enums.BookStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BookInventoryService {
    private List<Book> books;
    private Long nextBookId;

    @Inject
    public BookInventoryService() {
        this.books = new ArrayList<>();
        this.nextBookId = 1L;
    }

    public BookInventoryService(List<Book> books, Long nextBookId) {
        this.books = new ArrayList<>(books);
        this.nextBookId = nextBookId;
    }

    public void addBookToCatalog(Book b){
        b.setId(nextBookId++);
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
        System.out.println(book.getTitle());
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

    public Long getNextBookId() {
        return nextBookId;
    }
}

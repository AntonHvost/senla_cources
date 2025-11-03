package t1.model;

import t1.enums.BookStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookCatalog {
    private List<Book> books;

    public BookCatalog() {
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



}

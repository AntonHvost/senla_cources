package t_4.model;

import t_4.enums.BookStatus;
import t_4.service.RequestService;

import java.util.ArrayList;
import java.util.List;

public class BookCatalog {
    private List<Book> books;

    public BookCatalog() {
        this.books = new ArrayList<>();
    }

    public void addBookToCatalog(Book b){
        books.add(b);
    }

    public Book findBookById(long bookId){
        return books.stream().filter(b -> b.getId() == bookId).findFirst().orElse(null);
    }

    public void restockBook(long bookId){
        Book addingBook = books.stream().filter(b -> b.getId() == bookId).findFirst().orElse(null);
        if(addingBook != null) addingBook.setStatus(BookStatus.AVAILABLE);
    }

}

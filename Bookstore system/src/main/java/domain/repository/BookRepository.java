package domain.repository;

import di.annotation.Component;
import domain.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BookRepository implements Repository<Book> {

    private List<Book> bookList;
    private Long nextBookId;

    public BookRepository() {
        this.bookList = new ArrayList<>();
        this.nextBookId = 1L;
    }

    @Override
    public List<Book> findAll() {
        return bookList;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookList.stream()
                .filter(b -> b.getId().equals(id))
                .findAny();
    }

    public void replaceAll(List<Book> bookList, Long nextBookId) {
        this.bookList = bookList;
        this.nextBookId = nextBookId;
        System.out.println(this.nextBookId);
    }

    @Override
    public void save(Book book) {
        bookList.add(book);
    }

    @Override
    public void update(Book book) {
        for (Book b : bookList){
            if(b.getId().equals(book.getId())){
                bookList.set(bookList.indexOf(b), book);
                return;
            }
        }
    }

    @Override
    public Long generateNextId() {
        return nextBookId++;
    }

    @Override
    public Long getNextId() {
        return nextBookId;
    }

    @Override
    public void setNextId(Long nextId) {
        this.nextBookId = nextId;
    }
}

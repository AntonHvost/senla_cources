package repository.impl;

import di.annotation.Component;
import domain.model.impl.Book;
import repository.BaseRepository;

@Component
public class BookRepository extends BaseRepository<Book, Long> {
    public BookRepository() {
        super(Book.class);
    }
}

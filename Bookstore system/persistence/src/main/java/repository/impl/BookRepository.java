package repository.impl;

import domain.model.impl.Book;
import org.springframework.stereotype.Repository;
import repository.BaseRepository;

@Repository
public class BookRepository extends BaseRepository<Book, Long> {
    public BookRepository() {
        super(Book.class);
    }
}

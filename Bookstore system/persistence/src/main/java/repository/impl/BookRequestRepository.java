package repository.impl;

import domain.model.impl.BookRequest;
import org.springframework.stereotype.Repository;
import repository.BaseRepository;

@Repository
public class BookRequestRepository extends BaseRepository<BookRequest, Long> {
    BookRequestRepository() {
        super(BookRequest.class);
    }
}

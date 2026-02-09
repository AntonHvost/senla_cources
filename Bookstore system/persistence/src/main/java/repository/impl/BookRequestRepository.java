package repository.impl;

import di.annotation.Component;
import domain.model.impl.BookRequest;
import repository.BaseRepository;

@Component
public class BookRequestRepository extends BaseRepository<BookRequest, Long> {
    BookRequestRepository() {
        super(BookRequest.class);
    }
}

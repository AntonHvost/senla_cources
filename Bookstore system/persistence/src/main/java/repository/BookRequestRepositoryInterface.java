package repository;

import domain.model.impl.BookRequest;

import java.util.List;

public interface BookRequestRepositoryInterface extends Repository<BookRequest,Long> {
    List<BookRequest> findAllRequestWithBook();
}

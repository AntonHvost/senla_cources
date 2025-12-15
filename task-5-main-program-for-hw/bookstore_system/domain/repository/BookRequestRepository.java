package bookstore_system.domain.repository;

import bookstore_system.di.annotation.Component;
import bookstore_system.domain.model.Book;
import bookstore_system.domain.model.BookRequest;

import java.util.List;
import java.util.Optional;

@Component
public class BookRequestRepository implements Repository<BookRequest> {

    private List<BookRequest> requestList;
    private Long nextRequestId;

    @Override
    public List<BookRequest> findAll() {
        return requestList;
    }

    @Override
    public Optional<BookRequest> findById(Long id) {
        return requestList.stream().filter(r -> r.getId().equals(id)).findAny();
    }

    public void replaceAll(List<BookRequest> bookRequestList, Long nextRequestId) {
        this.requestList = bookRequestList;
        this.nextRequestId = nextRequestId;
    }

    @Override
    public void save(BookRequest request) {

    }

    @Override
    public void update(BookRequest request) {
        for (BookRequest r : requestList) {
            if (r.getId().equals(request.getId())) {
                requestList.set(requestList.indexOf(request), request);
                return;
            }
        }
    }

    @Override
    public Long generateNextId() {
        return 0L;
    }

    @Override
    public Long getNextId() {
        return 0L;
    }

    @Override
    public void setNextId(Long nextId) {

    }
}

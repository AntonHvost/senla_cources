package domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import di.annotation.Component;
import di.annotation.Inject;
import domain.model.BookRequest;
import domain.repository.BookRequestRepository;
import enums.RequestStatus;

@Component
public class RequestService {
    private BookRequestRepository bookRequestRepository;

    @Inject
    public RequestService(BookRequestRepository bookRequestRepository) {
        this.bookRequestRepository = bookRequestRepository;
    }

    public BookRequest createRequest(Long bookId, Long orderId){
        BookRequest req = new BookRequest(bookId, orderId);
        req.setId(bookRequestRepository.generateNextId());
        bookRequestRepository.save(req);
        return req;
    }

    public Optional<BookRequest> findRequestById(long id){
        return  bookRequestRepository.findById(id);
    }

    public List<BookRequest> findPendingRequestsByBookId(Long bookId) {
        return bookRequestRepository.findAll().stream()
                .filter(r -> r.getReqBookId().equals(bookId))
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    public RequestStatus getRequestStatusByOrderId(long id){
        BookRequest request = bookRequestRepository.findAll().stream().filter(r -> r.getId() == id).findAny().orElse(null);
        return request != null ? request.getStatus() : null;
    }

    public void cancelRequest(long requestId){
        findRequestById(requestId).ifPresent(BookRequest::cancel);
    }

    public List<BookRequest> getRequestsList() {
        return bookRequestRepository.findAll();
    }

    public void save(BookRequest request){
        if (request.getId() == null || request.getId() == 0) {
            request.setId(bookRequestRepository.generateNextId());
        }
        bookRequestRepository.save(request);
    }

    public void update(BookRequest request) {
        bookRequestRepository.update(request);
    }

}

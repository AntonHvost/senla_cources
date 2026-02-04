package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import di.annotation.Component;
import di.annotation.Inject;
import domain.model.impl.BookRequest;
import repository.impl.BookRequestRepository;
import enums.RequestStatus;


@Component
public class RequestService {
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    private final BookRequestRepository bookRequestRepository;

    @Inject
    public RequestService(BookRequestRepository bookRequestRepository) {
        this.bookRequestRepository = bookRequestRepository;
    }

    public BookRequest createRequest(Long bookId, Long orderId){
        logger.debug("Creating request for book ID {} linked to order ID {}", bookId, orderId);
        BookRequest req = new BookRequest(bookId, orderId);
        bookRequestRepository.save(req);
        logger.info("Request ID {} created successfully", req.getId());
        return req;
    }

    public Optional<BookRequest> findRequestById(long id){
        logger.debug("Fetching request by ID: {}", id);
        return  bookRequestRepository.findById(id);
    }

    public List<BookRequest> findPendingRequestsByBookId(Long bookId) {
        logger.debug("Fetching pending requests for book ID: {}", bookId);
        List<BookRequest> result = bookRequestRepository.findAll().stream()
                .filter(r -> r.getReqBookId().equals(bookId))
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .collect(Collectors.toList());
        logger.debug("Found {} pending requests for book ID {}", result.size(), bookId);
        return result;
    }

    public RequestStatus getRequestStatusByOrderId(long id){
        logger.debug("Fetching request status for order ID: {}", id);
        BookRequest request = bookRequestRepository.findAll().stream().filter(r -> r.getId() == id).findAny().orElse(null);

        return request != null ? request.getStatus() : null;
    }

    public List<BookRequest> getRequestsList() {
        logger.debug("Fetching all book requests");
        return bookRequestRepository.findAll();
    }

    public void save(BookRequest request){
        logger.debug("Saving book request: ID={}", request.getId());
        bookRequestRepository.save(request);
    }

    public void update(BookRequest request) {
        logger.debug("Updating book request: ID={}", request.getId());
        bookRequestRepository.update(request);
    }

}

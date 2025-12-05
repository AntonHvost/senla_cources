package bookstore_system.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import bookstore_system.domain.model.BookRequest;
import bookstore_system.enums.RequestStatus;


public class RequestService {
    private List<BookRequest> requestsList;

    public RequestService(){
        this.requestsList = new ArrayList<>();
    }

    public BookRequest createRequest(Long bookId, Long orderId){
        BookRequest req = new BookRequest(bookId, orderId);
        requestsList.add(req);
        return req;
    }

    public Optional<BookRequest> findRequestById(long id){
        return  requestsList.stream().filter(r -> r.getId() == id).findAny();
    }

    public List<BookRequest> findPendingRequestsByBookId(Long bookId) {
        return requestsList.stream()
                .filter(r -> r.getReqBookId().equals(bookId))
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    public RequestStatus getRequestStatusByOrderId(long id){
        BookRequest request = requestsList.stream().filter(r -> r.getId() == id).findAny().orElse(null);
        return request != null ? request.getStatus() : null;
    }

    public void cancelRequest(long requestId){
        findRequestById(requestId).ifPresent(BookRequest::cancel);
    }

    public List<BookRequest> getRequestsList() {
        return requestsList;
    }

    public void save(BookRequest request){
        requestsList.add(request);
    }

    public void update(BookRequest request) {
        requestsList.set(requestsList.indexOf(request), request);
    }

}

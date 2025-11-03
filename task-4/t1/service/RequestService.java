package t1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import t1.enums.RequestStatus;

import t1.model.Book;
import t1.model.BookRequest;
import t1.model.Order;

public class RequestService {
    private List<BookRequest> requestsList;
    private long nextRequestId;

    public RequestService(){
        this.requestsList = new ArrayList<>();
        this.nextRequestId = 0;
    }

    public BookRequest createRequest(Book book, Order order){
        BookRequest req = new BookRequest(nextRequestId++, book, order);
        requestsList.add(req);
        return req;
    }

    public Optional<BookRequest> findRequestById(long id){
        return  requestsList.stream().filter(r -> r.getId() == id).findAny();
    }

    public void fulfillRequests(){
        requestsList.stream()
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .forEach(BookRequest::fulFilled);
    }

    public RequestStatus getRequestStatusByOrderId(long id){
        return requestsList.stream().filter(r -> r.getRelatedOrder().getId() == id).findAny().get().getStatus();
    }

    public RequestStatus getStatusRequest(long requestId) {
        return findRequestById(requestId).map(BookRequest::getStatus).orElseThrow(() -> new IllegalStateException("Запрос с ID " + requestId + " не найден!"));
    }

    public void cancelRequest(long requestId){
        findRequestById(requestId).ifPresent(BookRequest::cancel);
    }


}

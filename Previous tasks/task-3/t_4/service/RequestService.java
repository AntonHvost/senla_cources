package t_4.service;

import t_4.enums.RequestStatus;
import t_4.model.Book;
import t_4.model.BookRequest;
import t_4.model.Order;

import java.util.ArrayList;
import java.util.List;

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

    public void fulfillRequests(){
        requestsList.stream()
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .forEach(BookRequest::fulFilled);
    }

    public RequestStatus getStatusRequest(long requestId){
        return requestsList.stream().filter(r -> r.getId() == requestId).findFirst().get().getStatus();
    }

    public void cancelRequest(long requestId){
        requestsList.stream().filter(r -> r.getId() == requestId).forEach(BookRequest::cancel);
    }


}

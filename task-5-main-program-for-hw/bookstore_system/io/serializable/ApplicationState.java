package bookstore_system.io.serializable;

import bookstore_system.domain.model.Book;
import bookstore_system.domain.model.BookRequest;
import bookstore_system.domain.model.Consumer;
import bookstore_system.domain.model.Order;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ApplicationState {
    private Long nextBookId;
    private Long nextOrderId;
    private Long nextOrderItemId;
    private Long nextRequestId;
    private Long nextConsumerId;


    private List<Book> books;
    private List<Consumer> consumers;
    private List<Order> orders;
    private List<BookRequest> requests;

    public ApplicationState() {
        this.books = new ArrayList<>();
        this.consumers = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.nextBookId = 1L;
        this.nextOrderId = 1L;
        this.nextOrderItemId = 1L;
        this.nextRequestId = 1L;
        this.nextConsumerId = 1L;
    }

    public ApplicationState(
            List<Book> books,
            Long nextBookId,
            List<Order> orders,
            Long nextOrderId,
            Long nextOrderItemId,
            List<Consumer> consumers,
            Long nextConsumerId,
            List<BookRequest> requests,
            Long nextRequestId
            ) {
        this.books = books;
        this.orders = orders;
        this.consumers = consumers;
        this.requests =  requests;
        this.nextBookId = nextBookId;
        this.nextOrderId = nextOrderId;
        this.nextOrderItemId = nextOrderItemId;
        this.nextRequestId = nextRequestId;
        this.nextConsumerId = nextConsumerId;
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<BookRequest> getRequests() {
        return requests;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public Long getNextBookId() {
        return nextBookId;
    }
    public Long getNextOrderId() {
        return nextOrderId;
    }

    public Long getNextOrderItemId() {
        return nextOrderItemId;
    }

    public Long getNextRequestId() {
        return nextRequestId;
    }

    public Long getNextConsumerId() {
        return nextConsumerId;
    }

}


package io;

import repository.BookRepository;
import repository.BookRequestRepository;
import repository.ConsumerRepository;
import repository.OrderRepository;
import io.serializable.ApplicationState;
import io.serializable.SerializableManager;

public class PersistanceManager {

    private final SerializableManager serializableManager;

    private final ConsumerRepository consumerRepository;
    private final BookRepository bookRepository;
    private final BookRequestRepository bookRequestRepository;
    private final OrderRepository orderRepository;

    public PersistanceManager(SerializableManager manager,ConsumerRepository consumerRepository,BookRepository bookRepository, BookRequestRepository bookRequestRepository, OrderRepository orderRepository) {
        this.serializableManager = manager;
        this.consumerRepository = consumerRepository;
        this.bookRepository = bookRepository;
        this.bookRequestRepository = bookRequestRepository;
        this.orderRepository = orderRepository;
    }

    public void initialState() {
        ApplicationState state = serializableManager.loadState();
        /*bookRepository.replaceAll(state.getBooks(), state.getNextBookId());
        bookRequestRepository.replaceAll(state.getRequests(), state.getNextRequestId());
        consumerRepository.replaceAll(state.getConsumers(), state.getNextConsumerId());
        orderRepository.replaceAll(state.getOrders(), state.getNextOrderId(), state.getNextOrderItemId());*/

    }

    /*public void saveState() {
        ApplicationState state = new ApplicationState(
                bookRepository.findAll(),
                bookRepository.getNextId(),
                orderRepository.findAll(),
                orderRepository.getNextId(),
                orderRepository.getNextItemId(),
                consumerRepository.findAll(),
                consumerRepository.getNextId(),
                bookRequestRepository.findAll(),
                bookRequestRepository.getNextId()
        );
        serializableManager.saveState(state);
    }*/
}

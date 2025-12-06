package bookstore_system.ui.controller;

import bookstore_system.domain.model.Consumer;
import bookstore_system.domain.service.ConsumerService;
import bookstore_system.facade.ConsumerFacade;

import java.util.List;

public class ConsumerController {
    private final ConsumerFacade consumerFacade;

    public ConsumerController(ConsumerFacade consumerFacade) {
        this.consumerFacade = consumerFacade;
    }

    public List<Consumer> getConsumers() {
        return consumerFacade.getConsumers();
    }

    public void importConsumer (String filename) {
        consumerFacade.importConsumer(filename);
    }

    public void exportConsumer (String filename) {
        consumerFacade.exportConsumer(filename);
    }
}

package ui.controller;

import di.annotation.Component;
import di.annotation.Inject;
import domain.model.Consumer;
import facade.ConsumerFacade;

import java.util.List;

@Component
public class ConsumerController {
    private final ConsumerFacade consumerFacade;
    @Inject
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

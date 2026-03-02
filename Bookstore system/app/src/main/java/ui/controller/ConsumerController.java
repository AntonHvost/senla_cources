package ui.controller;

import domain.model.impl.Consumer;
import facade.ConsumerFacade;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
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

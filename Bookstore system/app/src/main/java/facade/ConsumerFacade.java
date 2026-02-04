package facade;

import di.annotation.Component;
import di.annotation.Inject;
import domain.model.impl.Consumer;
import service.ConsumerService;
import service.IOService;
import io.csv.converter.ConsumerCSVConverter;

import java.util.List;

@Component
public class ConsumerFacade {
    private final ConsumerService consumerService;
    private final IOService ioService;
    private final ConsumerCSVConverter consumerCSVConverter = new ConsumerCSVConverter();

    @Inject
    public ConsumerFacade(ConsumerService consumerService, IOService ioService) {
        this.consumerService = consumerService;
        this.ioService = ioService;
    }

    public List<Consumer> getConsumers() {
       return consumerService.findAllConsumers();
    }

    public void importConsumer (String filename) {
        ioService.importEntities(filename, consumerService::findConsumerById, consumerService::save, consumerService::update, consumerCSVConverter);
    }

    public void exportConsumer (String filename) {
        ioService.exportEntities(filename, consumerService::findAllConsumers, consumerCSVConverter);
    }
}

package bookstore_system.facade;

import bookstore_system.di.annotation.Component;
import bookstore_system.di.annotation.Inject;
import bookstore_system.domain.model.Consumer;
import bookstore_system.domain.service.ConsumerService;
import bookstore_system.domain.service.IOService;
import bookstore_system.io.csv.converter.ConsumerCSVConverter;

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

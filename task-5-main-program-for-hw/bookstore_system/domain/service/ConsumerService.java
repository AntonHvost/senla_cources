package bookstore_system.domain.service;

import bookstore_system.di.annotation.Component;
import bookstore_system.domain.model.Consumer;

import java.util.List;
import java.util.Optional;

@Component
public class ConsumerService {
    private List<Consumer> consumers;
    private Long nextConsumerId;

    public ConsumerService() {}

    public ConsumerService(List<Consumer> consumers, Long nextConsumerId) {
        this.consumers = consumers;
        this.nextConsumerId = nextConsumerId;
    }

    public Optional<Consumer> findConsumerById(Long consumerId) {
        return consumers.stream().filter(c -> c.getId().equals(consumerId)).findAny();
    }

    public List<Consumer> findAllConsumers() {
        return consumers;
    }

    public Long getNextConsumerId() {
        return nextConsumerId;
    }

    public void save(Consumer consumer) {
        consumer.setId(nextConsumerId++);
        consumers.add(consumer);
    }

    public void update(Consumer consumer) {
        for (Consumer c : consumers) {
            if (c.getId().equals(consumer.getId())) {
                consumers.set(consumers.indexOf(c), consumer);
                return;
            }
        }
    }
}

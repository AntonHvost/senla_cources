package bookstore_system.domain.service;

import bookstore_system.domain.model.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsumerService {
    private final List<Consumer> consumers = new ArrayList<>();

    public Optional<Consumer> findConsumerById(Long consumerId) {
        return consumers.stream().filter(c -> c.getId().equals(consumerId)).findAny();
    }

    public List<Consumer> findAllConsumers() {
        return consumers;
    }

    public void save(Consumer consumer) {
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

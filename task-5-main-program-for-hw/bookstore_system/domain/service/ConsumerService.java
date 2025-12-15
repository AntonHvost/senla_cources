package bookstore_system.domain.service;

import bookstore_system.di.annotation.Component;
import bookstore_system.domain.model.Consumer;
import bookstore_system.domain.repository.ConsumerRepository;

import java.util.List;
import java.util.Optional;

@Component
public class ConsumerService {
    private ConsumerRepository consumerRepository;

    public ConsumerService(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    public Optional<Consumer> findConsumerById(Long consumerId) {
        return consumerRepository.findById(consumerId);
    }

    public List<Consumer> findAllConsumers() {
        return consumerRepository.findAll();
    }

    public void save(Consumer consumer) {
        if(consumer.getId() == null || consumer.getId() == 0) {
            consumer.setId(consumerRepository.generateNextId());
        }
        consumerRepository.save(consumer);
    }

    public void update(Consumer consumer) {
       consumerRepository.update(consumer);
    }
}

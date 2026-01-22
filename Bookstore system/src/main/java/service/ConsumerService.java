package service;

import di.annotation.Component;
import domain.model.Consumer;
import repository.ConsumerRepository;

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

    public Consumer save(Consumer consumer) {
        consumerRepository.save(consumer);
        return consumer;
    }

    public void update(Consumer consumer) {
       consumerRepository.update(consumer);
    }
}

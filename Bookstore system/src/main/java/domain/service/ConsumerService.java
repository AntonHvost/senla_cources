package domain.service;

import di.annotation.Component;
import domain.model.Consumer;
import domain.repository.ConsumerRepository;

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

package service;

import domain.model.impl.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.impl.ConsumerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class ConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    private final ConsumerRepository consumerRepository;

    @Autowired
    public ConsumerService(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    public Optional<Consumer> findConsumerById(Long consumerId) {
        logger.debug("Fetching consumer by ID: {}", consumerId);
        return consumerRepository.findById(consumerId);
    }

    public List<Consumer> findAllConsumers() {
        logger.debug("Fetching all consumers");
        return consumerRepository.findAll();
    }

    public void save(Consumer consumer) {
        logger.info("Saving new consumer: {}", consumer.getName());
        consumerRepository.save(consumer);
    }

    public void update(Consumer consumer) {
        logger.debug("Updating consumer ID: {}", consumer.getId());
        consumerRepository.update(consumer);
    }
}

package bookstore_system.domain.service;

import bookstore_system.domain.model.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsumerService {
    private final List<Consumer> consumers = new ArrayList<>();

    public Optional<Consumer> findConsumerById(long consumerId) {
        return consumers.stream().filter(c -> c.getId() == consumerId).findFirst();
    }
}

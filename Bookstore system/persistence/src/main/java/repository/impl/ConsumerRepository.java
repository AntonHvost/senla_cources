package repository.impl;

import di.annotation.Component;
import domain.model.impl.BookRequest;
import domain.model.impl.Consumer;
import repository.BaseRepository;

@Component
public class ConsumerRepository extends BaseRepository<Consumer, Long> {
    public ConsumerRepository() {
        super(Consumer.class);
    }
}

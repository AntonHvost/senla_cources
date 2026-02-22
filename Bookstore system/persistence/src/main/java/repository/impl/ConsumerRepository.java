package repository.impl;

import domain.model.impl.Consumer;
import org.springframework.stereotype.Repository;
import repository.BaseRepository;

@Repository
public class ConsumerRepository extends BaseRepository<Consumer, Long> {
    public ConsumerRepository() {
        super(Consumer.class);
    }
}

package bookstore_system.domain.repository;

import bookstore_system.di.annotation.Component;
import bookstore_system.domain.model.Book;
import bookstore_system.domain.model.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConsumerRepository implements Repository<Consumer> {
    private List<Consumer> consumerList;
    private Long nextConsumerId;

    public ConsumerRepository() {
        this.consumerList = new ArrayList<>();
        this.nextConsumerId = 1L;
    }

    @Override
    public List<Consumer> findAll() {
        return consumerList;
    }

    @Override
    public Optional<Consumer> findById(Long id) {
        return consumerList.stream().filter(c -> c.getId().equals(id)).findAny();
    }

    public void replaceAll(List<Consumer> consumerList, Long nextConsumerId) {
        this.consumerList = consumerList;
        this.nextConsumerId = nextConsumerId;
    }

    @Override
    public void save(Consumer consumer) {
        consumerList.add(consumer);
    }

    @Override
    public void update(Consumer consumer) {
        for(Consumer c : consumerList){
            if(c.getId().equals(consumer.getId())){
                consumerList.set(consumerList.indexOf(consumer), consumer);
                return;
            }
        }
    }

    @Override
    public Long generateNextId() {
        return nextConsumerId++;
    }

    @Override
    public Long getNextId() {
        return nextConsumerId;
    }

    @Override
    public void setNextId(Long nextId) {
        this.nextConsumerId = nextId;
    }
}

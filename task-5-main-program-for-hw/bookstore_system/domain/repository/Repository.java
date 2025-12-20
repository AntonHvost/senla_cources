package bookstore_system.domain.repository;

import bookstore_system.domain.model.Book;
import bookstore_system.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    List<T> findAll();
    Optional<T> findById(Long id);
    void save(T entity);
    void update(T entity);
    Long generateNextId();
    Long getNextId();
    void setNextId(Long nextId);
}

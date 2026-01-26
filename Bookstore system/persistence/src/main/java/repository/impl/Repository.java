package repository.impl;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    List<T> findAll();
    Optional<T> findById(Long id);
    T save(T entity);
    T update(T entity);
    void delete(Long id);
}

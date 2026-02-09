package repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<T,PK extends Serializable> {
    List<T> findAll();
    Optional<T> findById(PK id);
    PK save(T entity);
    void update(T entity);
    void delete(PK id);
}

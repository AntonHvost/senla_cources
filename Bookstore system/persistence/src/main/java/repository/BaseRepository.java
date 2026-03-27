package repository;

import domain.model.Identifiable;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T extends Identifiable, PK extends Serializable> implements Repository<T, PK> {

    protected final Class<T> type;
    @PersistenceContext
    protected EntityManager em;

    public BaseRepository(Class<T> type) {
        this.type = type;
    }

    @Transactional
    @Override
    public List<T> findAll() {
        return em.createQuery("from " + type.getSimpleName(), type).getResultList();
    }

    @Override
    public Optional<T> findById(PK id) {
        return Optional.ofNullable(em.find(type, id));
    }

    @Transactional
    @Override
    public PK save(T entity) {
        em.persist(entity);
        return (PK) entity.getId();
    }

    @Transactional
    @Override
    public void update(T entity) {
        em.merge(entity);
    }

    @Transactional
    @Override
    public void delete(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }
}

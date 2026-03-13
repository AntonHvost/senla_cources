package repository;

import domain.model.Identifiable;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import util.HibernateUtil;

import jakarta.persistence.EntityManager;
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

    @Override
    public List<T> findAll() {
        return em.createQuery("from " + type.getSimpleName(), type).getResultList();
    }

    @Override
    public Optional<T> findById(PK id) {
        return Optional.ofNullable(em.find(type, id));
    }

    @Override
    public PK save(T entity) {
        em.persist(entity);
        return (PK) entity.getId();
    }

    @Override
    public void update(T entity) {
        em.merge(entity);
    }

    @Override
    public void delete(PK id) {
        em.remove(id);
    }
}

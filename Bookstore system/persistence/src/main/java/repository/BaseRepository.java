package repository;

import domain.model.Identifiable;
import org.hibernate.Session;
import util.HibernateUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T extends Identifiable, PK extends Serializable> implements Repository<T, PK> {

    protected final Class<T> type;
    protected Session session;

    public BaseRepository(Class<T> type) {
        this.type = type;
        this.session = HibernateUtil.getSession();
    }

    @Override
    public List<T> findAll() {
        return session.createQuery("from " + type.getSimpleName(), type).list();
    }

    @Override
    public Optional<T> findById(PK id) {
        return Optional.ofNullable(session.load(type, id));
    }

    @Override
    public PK save(T entity) {
        return (PK) session.save(entity);
    }

    @Override
    public void update(T entity) {
        session.update(entity);
    }

    @Override
    public void delete(PK id) {
        session.delete(id);
    }
}

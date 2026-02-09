package repository;

import domain.model.Identifiable;
import util.HibernateUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T extends Identifiable, PK extends Serializable> implements Repository<T, PK> {

    protected final Class<T> type;

    public BaseRepository(Class<T> type) {
        this.type = type;
    }

    @Override
    public List<T> findAll() {
        return HibernateUtil.getSession().createCriteria(type).list();
    }

    @Override
    public Optional<T> findById(PK id) {
        return Optional.ofNullable(HibernateUtil.getSession().load(type, id));
    }

    @Override
    public PK save(T entity) {
        return (PK) HibernateUtil.getSession().save(entity);
    }

    @Override
    public void update(T entity) {
        HibernateUtil.getSession().update(entity);
    }

    @Override
    public void delete(PK id) {
        HibernateUtil.getSession().delete(id);
    }
}

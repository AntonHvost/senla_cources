package repository;

import domain.model.Transfer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@org.springframework.stereotype.Repository
public class TransferRepositoryImpl implements Repository<Transfer, Long> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Transfer> findAll() {
        return em.createQuery("from Transfer", Transfer.class).getResultList();
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return Optional.ofNullable(em.find(Transfer.class, id));
    }

    @Override
    public Long save(Transfer entity) {
        em.persist(entity);
        return entity.getId();
    }

    @Override
    public Long save(Iterable<Transfer> entities) {
        em.persist(entities);
        return entities.iterator().next().getId();
    }

    @Override
    public void update(Transfer entity) {
        em.merge(entity);
    }

    @Override
    public void delete(Transfer entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }
}

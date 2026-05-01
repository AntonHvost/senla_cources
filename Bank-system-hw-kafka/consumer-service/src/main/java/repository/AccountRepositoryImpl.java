package repository;

import domain.model.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Transactional
@org.springframework.stereotype.Repository
public class AccountRepositoryImpl implements Repository<Account, Long> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Account> findAll() {
        return em.createQuery("from Account", Account.class).getResultList();
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(em.find(Account.class, id));
    }

    @Override
    public Long save(Account entity) {
        em.persist(entity);
        return entity.getId();
    }

    @Override
    public Long save(Iterable<Account> entities) {
        em.persist(entities);
        return entities.iterator().next().getId();
    }

    @Override
    public void update(Account entity) {
        em.merge(entity);
    }

    @Override
    public void delete(Account entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }
}

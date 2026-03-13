package repository.impl;

import domain.model.impl.BookRequest;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import repository.BaseRepository;
import repository.BookRequestRepositoryInterface;
import util.HibernateUtil;

import java.util.List;

@Repository
public class BookRequestRepository extends BaseRepository<BookRequest, Long> implements BookRequestRepositoryInterface {
    BookRequestRepository() {
        super(BookRequest.class);
    }

    @Override
    public List<BookRequest> findAllRequestWithBook() {
        String qr = "select b from BookRequest b left join fetch b.reqBook";

        return em.createQuery(qr, BookRequest.class).getResultList();
    }
}

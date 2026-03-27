package repository.impl;

import domain.model.impl.RefreshToken;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import repository.BaseRepository;
import repository.RefreshTokenRepository;

import java.util.Optional;

@Repository
public class RefreshTokenRepositoryImpl extends BaseRepository<RefreshToken,Long> implements RefreshTokenRepository {
    public RefreshTokenRepositoryImpl() {super(RefreshToken.class);}

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        String qr = "select r from RefreshToken r where r.token = :token";
        try {
            Query query = em.createQuery(qr);
            query.setParameter("token", token);
            return Optional.of((RefreshToken) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}

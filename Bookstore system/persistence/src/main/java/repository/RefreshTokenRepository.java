package repository;

import domain.model.impl.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends Repository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
}

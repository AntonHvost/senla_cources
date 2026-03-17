package repository;

import domain.model.impl.User;

import java.util.Optional;

public interface UserRepository extends Repository<User,Long>{
    Optional<User> findByEmail(String email);
}

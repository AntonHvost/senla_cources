package repository.impl;

import domain.model.impl.User;
import org.springframework.stereotype.Repository;
import repository.BaseRepository;
import repository.UserRepository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepository<User, Long> implements UserRepository {


    UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String qr = "select u from User u where u.email=:email";
        Optional<User> user = Optional.ofNullable(em.createQuery(qr, User.class).setParameter("email", email).getSingleResult());
        return user;
    }
}

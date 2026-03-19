package service;

import domain.model.impl.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServiceInterface extends UserDetailsService {
    void save(User user);
}

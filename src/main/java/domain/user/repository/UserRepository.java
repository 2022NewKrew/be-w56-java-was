package domain.user.repository;

import domain.user.model.User;
import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    void save(User user);

    Optional<User> findUserByUserId(String userId);

    Collection<User> findAll();
}

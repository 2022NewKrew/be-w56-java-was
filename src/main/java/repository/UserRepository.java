package repository;

import java.util.List;

import java.util.Optional;
import model.User;

public interface UserRepository {

    void save(User user);

    Optional<User> findUserById(String userId);

    List<User> findAll();
}

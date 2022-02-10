package db.user;

import model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void addUser(User user);
    Optional<User> findUserById(String userId);
    List<User> findAll();
}

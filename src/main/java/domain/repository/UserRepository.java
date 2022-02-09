package domain.repository;

import domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAllUsers();
    Optional<User> findUserById(String userId);
    void addUser(User user);
}

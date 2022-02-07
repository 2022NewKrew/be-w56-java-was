package webserver.domain.repository;

import webserver.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getUsers();
    Optional<User> getUser(String id);
    void saveUser(User user);
}

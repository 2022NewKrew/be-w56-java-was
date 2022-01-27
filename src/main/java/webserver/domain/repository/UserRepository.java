package webserver.domain.repository;

import webserver.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> getUser(String id);
    void saveUser(User user);
}

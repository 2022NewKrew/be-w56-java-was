package webserver.domain.repository;

import webserver.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAll();
    Optional<User> getBy(String id);
    void save(User user);
    void saveAll(List<User> users);
    void delete(String userId);
    void deleteAll();
}

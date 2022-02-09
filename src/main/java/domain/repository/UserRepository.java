package domain.repository;

import domain.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAllUsers();
    User findUserById(String userId);
    void addUser(User user);
}

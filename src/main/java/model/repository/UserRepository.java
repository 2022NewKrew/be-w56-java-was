package model.repository;

import model.User;

import java.util.List;

public interface UserRepository {
    User save(User user);
    User findById(int id);
    List<User> findAll();
}

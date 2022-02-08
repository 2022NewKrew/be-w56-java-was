package application.out.user;

import domain.user.User;

import java.util.List;

public interface UserDao {
    void save(User user);
    User findByUserId(String userId);
    List<User> findAll();
}

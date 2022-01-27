package application.out.user;

import domain.user.User;

public interface UserDao {
    void save(User user);
    User findByUserId(String userId);
}

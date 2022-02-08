package application.repository;

import java.util.List;
import application.model.User;

public interface UserRepository {

    void addUser(User user);

    User findUserById(String userId);

    List<User> findAll();

}

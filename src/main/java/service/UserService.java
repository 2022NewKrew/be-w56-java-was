package service;

import java.util.List;
import java.util.Optional;
import model.User;

public interface UserService {

    Optional<User> login(User user);

    List<User> findAll();

    void save(User user);

}

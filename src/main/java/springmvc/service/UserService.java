package springmvc.service;

import model.User;
import springmvc.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public void userRegister(String userId, String password, String name, String email) {
        userRepository.save(new User(userId, password, name, email));
    }
}

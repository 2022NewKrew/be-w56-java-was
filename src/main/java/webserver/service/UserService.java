package webserver.service;

import lombok.extern.slf4j.Slf4j;
import model.user.User;
import webserver.repository.UserRepository;

import java.util.List;

@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private static final UserService userService = new UserService();

    private UserService() {
        userRepository = UserRepository.getInstance();
    }

    public static UserService getInstance() {
        return userService;
    }

    public User joinUser(User user) {
        //DataBase.addUser(user);
        userRepository.save(user);
        log.info("회원가입 성공 : {}", user.getUserId());
        return user;
    }

    public boolean loginUser(String userId, String password) {
        //User user = DataBase.findUserById(userId);
        User user = userRepository.findById(userId);
        if (user == null)
            return false;
        return user.getPassword().equals(password);
    }

    public List<User> getAllUsers() {
        //Collection<User> users = DataBase.findAll();
        return userRepository.findAll();
        //return new ArrayList<>(users);
    }
}

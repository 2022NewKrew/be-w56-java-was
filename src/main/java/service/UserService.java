package service;

import db.user.UserRepository;
import db.user.UserRepositoryImpl;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.request.Queries;
import util.exception.UserNotFoundException;

import java.util.List;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final UserService userService = new UserService();
    private static final UserRepository userRepository = UserRepositoryImpl.getInstance();

    private UserService(){}

    public static UserService getInstance(){
        return userService;
    }


    public User createUser(Queries queries){
        User user = new User(
                queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email")
        );
        userRepository.addUser(user);
        log.info("[USER_SERVICE] : " + user);
        return user;
    }

    public User searchUserById(Queries queries){
        return userRepository.findUserById(queries.get("userId"))
                .orElseThrow(()-> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public boolean login(Queries queries){
        User user = userRepository.findUserById(queries.get("userId"))
                .orElseThrow(()-> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        boolean result = user != null && user.getPassword().equals(queries.get("password"));
        log.info("LOGIN : " + result);
        return result;
    }

    public List<User> searchAllUsers(){
        return userRepository.findAll();
    }
}

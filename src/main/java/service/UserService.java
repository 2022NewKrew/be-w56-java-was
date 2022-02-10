package service;

import db.DataBase;
import db.UserRepository;
import db.UserRepositoryImpl;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.request.Queries;

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
        return DataBase.findUserById(queries.get("userId"));
    }

    public boolean login(Queries queries){
        User user = DataBase.findUserById(queries.get("userId"));
        boolean result = user != null && user.getPassword().equals(queries.get("password"));
        log.info("LOGIN : " + result);
        return result;
    }

    public List<User> searchAllUsers(){
        return DataBase.findAll();
    }
}

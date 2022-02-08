package service;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final List<User> users;

    public UserService() {
        this.users = new ArrayList<>();
    }

    public void save(Map<String, String> queryStrings) {
        String userId = queryStrings.get("userId");
        String password = queryStrings.get("password");
        String name = queryStrings.get("name");
        String email = URLDecoder.decode(queryStrings.get("email"), StandardCharsets.UTF_8);

        users.add(new User(userId, password, name, email));

        log.info("save user by GET : {}", users);
    }

    public void save(User user) {
        users.add(new User(user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));

        log.info("save user by POST : {}", users);
    }

    public User findUserByIdAndPassword(String userId, String password) {
        return users.stream()
                .filter(user -> user.getUserId().equals(userId) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}

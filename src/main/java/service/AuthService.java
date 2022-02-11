package service;

import model.Request;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.UserRepository;
import util.HttpRequestUtils;

import java.util.Map;

public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final UserRepository userRepository;

    static {
        userRepository = new UserRepository();
    }

    public static void createUser(Request request) {
        Map<String, String> reqParam;
        if (request.method().equals("GET")) {
            reqParam = request.getParams();
        } else {
            reqParam = HttpRequestUtils.parseQueryString(request.getBody());
        }
        String userId = reqParam.get("userId");
        String password = reqParam.get("password");
        String name = reqParam.get("name");
        String email = reqParam.get("email");
        User user = new User(userId, password, name, email);
        userRepository.create(user);
    }

    public static boolean login(Request request) {
        Map<String, String> reqParam;
        if (request.method().equals("GET")) {
            reqParam = request.getParams();
        } else {
            reqParam = HttpRequestUtils.parseQueryString(request.getBody());
        }
        try {
            String userId = reqParam.get("userId");
            String password = reqParam.get("password");
            User userRetrieved = userRepository.findById(userId).orElse(null);
            return userRetrieved.getUserId().equals(userId) && userRetrieved.getPassword().equals(password);
        } catch (NullPointerException e) {
            logger.error("Could not retrieve user : {} " + e.getMessage());
            return false;
        }
    }
}

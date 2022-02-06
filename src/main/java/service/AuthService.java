package service;

import db.DataBase;
import model.Request;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.Map;
import java.util.NoSuchElementException;

public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

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
        DataBase.addUser(user);
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
            User userRetrieved = DataBase.findUserById(userId);
            return userRetrieved.getUserId().equals(userId) && userRetrieved.getPassword().equals(password);
        } catch (NoSuchElementException e) {
            logger.info(e.getMessage());
            return false;
        }
    }
}

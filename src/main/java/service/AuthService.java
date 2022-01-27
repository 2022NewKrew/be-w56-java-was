package service;

import db.DataBase;
import model.Request;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public static void login(Request request) {
        Map<String, String> reqParam;
        if (request.method().equals("GET")) {
            reqParam = request.getParams();
        }
        else {
            reqParam = HttpRequestUtils.parseQueryString(request.getBody());
        }
        try {
            String userid = reqParam.get("userId");
            String password = reqParam.get("password");
            String name = reqParam.get("name");
            String email = reqParam.get("email");
            User user = new User(userid, password, name, email);
            DataBase.addUser(user);
        }
        catch (NoSuchElementException e) {
            logger.info(e.getMessage());
        }
    }
}

package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.Map;

public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static void addUser(String body){
        Map<String, String> queries = HttpRequestUtils.parseBody(body);
        String userId = queries.get("userId");
        String name = queries.get("name");
        String password = queries.get("password");
        String email = queries.get("email");

        User user = new User(userId, password, name, email);
        log.info("Insert User : {} ", user);
        DataBase.addUser(user);
    }
}

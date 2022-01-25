package service;

import db.DataBase;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class SignUpService {

    private static final Logger log = LoggerFactory.getLogger(SignUpService.class);


    public static void signUp(String queryString) {
        Map<String, String> queryData = HttpRequestUtils.parseQueryString(queryString);

        User user = new User(
            queryData.get("userId"),
            queryData.get("password"),
            queryData.get("name"),
            queryData.get("email")
        );

        DataBase.addUser(user);

        log.debug("User {} {} {} signed up", user.getUserId(), user.getName(), user.getEmail());
    }
}

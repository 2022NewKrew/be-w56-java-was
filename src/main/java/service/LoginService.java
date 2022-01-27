package service;

import db.DataBase;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    public static boolean login(Map<String, String> queryData) {
        String userId = queryData.get("userId");
        String password = queryData.get("password");

        log.debug("User {} tries to login", userId);

        User user = DataBase.findUserById(userId);

        if (user == null) {
            log.debug("User {} Not Found", userId);
            return false;
        }

        if (!user.passwordMatch(password)) {
            log.debug("User {} password mismatch", userId);
            return false;
        }

        log.debug("Login Success");
        return true;
    }
}

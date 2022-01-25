package util;

import model.User;
import org.slf4j.Logger;

public class LoginUtils {

    public static String LOGIN_FAIL_COOKIE = "logined=false";
    public static String LOGIN_SUCCESS_COOKIE = "logined=true";

    public static String checkLogin(Logger log, User user, String password) {
        if(user == null) {
            log.debug("User Not Found");
            return LOGIN_FAIL_COOKIE;
        }
        if(user.getPassword().equals(password)) {
            log.debug("Login Success");
            return LOGIN_SUCCESS_COOKIE;
        }
        log.debug("Password MisMatch");
        return LOGIN_FAIL_COOKIE;
    }

}

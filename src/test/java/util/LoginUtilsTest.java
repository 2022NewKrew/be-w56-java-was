package util;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.*;

class LoginUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(LoginUtilsTest.class);

    @Test
    public void checkLogin() {
        User user = new User("test", "1234", "테스트", "test@email.com");
        DataBase.addUser(user);
        assertThat(LoginUtils.checkLogin(logger, user, "1234")).isEqualTo(LoginUtils.LOGIN_SUCCESS_COOKIE);
    }

    @Test
    public void checkLogin_passwordMismatch() {
        User user = new User("test", "1234", "테스트", "test@email.com");
        DataBase.addUser(user);
        assertThat(LoginUtils.checkLogin(logger, user, "123")).isEqualTo(LoginUtils.LOGIN_FAIL_COOKIE);
    }

    @Test
    public void checkLogin_userNotFound() {
        assertThat(LoginUtils.checkLogin(logger, null, "1234")).isEqualTo(LoginUtils.LOGIN_FAIL_COOKIE);
    }
}
package service;

import dao.MemoDao;
import dao.UserDao;
import db.DataBase;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DatabaseUtils;
import util.LoginUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class RequestServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(RequestServiceTest.class);
    private Connection connection = DatabaseUtils.connect();
    private UserDao userDao = new UserDao(connection);
    private MemoDao memoDao = new MemoDao(connection);
    private RequestService requestService = new RequestService(userDao, memoDao);

    RequestServiceTest() throws SQLException {
    }

    @Test
    public void createUser() throws IOException, SQLException {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "test");
        params.put("password", "1234");
        params.put("name", "테스트");
        params.put("email", "test@email.com");
        requestService.createUser(params, logger);

        User user = DataBase.findUserById("test");
        assertThat(user.getUserId()).isEqualTo("test");
        assertThat(user.getPassword()).isEqualTo("1234");
        assertThat(user.getName()).isEqualTo("테스트");
        assertThat(user.getEmail()).isEqualTo("test@email.com");
    }

    @Test
    public void createUser_noParams() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "test");
        params.put("password", "1234");

        Assertions.assertThrows(IOException.class, () -> requestService.createUser(params, logger));
    }

    @Test
    public void userLogin() throws IOException, SQLException {
        Map<String, String> createParams = new HashMap<>();
        createParams.put("userId", "testLogin");
        createParams.put("password", "1234");
        createParams.put("name", "테스트");
        createParams.put("email", "test@email.com");
        requestService.createUser(createParams, logger);

        Map<String, String> loginParams = new HashMap<>();
        loginParams.put("userId", "testLogin");
        loginParams.put("password", "1234");
        assertThat(requestService.userLogin(loginParams, logger)).isEqualTo(LoginUtils.LOGIN_SUCCESS_COOKIE);
    }

    @Test
    public void userLogin_noParams() throws IOException, SQLException {
        Map<String, String> createParams = new HashMap<>();
        createParams.put("userId", "testLogin");
        createParams.put("password", "1234");
        createParams.put("name", "테스트");
        createParams.put("email", "test@email.com");
        requestService.createUser(createParams, logger);

        Map<String, String> loginParams = new HashMap<>();
        loginParams.put("userId", "testLogin");
        Assertions.assertThrows(IOException.class, () -> requestService.userLogin(loginParams, logger));
    }

    @Test
    public void userLogin_passwordMismatch() throws IOException, SQLException {
        Map<String, String> createParams = new HashMap<>();
        createParams.put("userId", "testLogin");
        createParams.put("password", "1234");
        createParams.put("name", "테스트");
        createParams.put("email", "test@email.com");
        requestService.createUser(createParams, logger);

        Map<String, String> loginParams = new HashMap<>();
        loginParams.put("userId", "testLogin");
        loginParams.put("password", "12345");
        assertThat(requestService.userLogin(loginParams, logger)).isEqualTo(LoginUtils.LOGIN_FAIL_COOKIE);
    }

    @Test
    public void userLogin_userNotFound() throws IOException, SQLException {
        Map<String, String> loginParams = new HashMap<>();
        loginParams.put("userId", "testLoginUserNotFound");
        loginParams.put("password", "12345");
        assertThat(requestService.userLogin(loginParams, logger)).isEqualTo(LoginUtils.LOGIN_FAIL_COOKIE);
    }

}
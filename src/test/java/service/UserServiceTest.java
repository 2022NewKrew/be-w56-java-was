package service;

import db.DataBase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    UserService userService = new UserService();

    @Test
    void createUser() {
        Map<String, String> userInfoMap = new HashMap<>();
        userInfoMap.put("userId", "cih468");
        userInfoMap.put("password", "1234");
        userInfoMap.put("name", "최성현");
        userInfoMap.put("email", "cih468@naver.com");
        userService.createUser(userInfoMap);

        assertEquals(1, DataBase.getInstance().findAll().size());
        assertEquals("1234", DataBase.getInstance().findUserById("cih468").getPassword());
    }
}
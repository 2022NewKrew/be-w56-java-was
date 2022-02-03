package controller;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class UserControllerTest {

    UserController userController = new UserController();
    @Test
    void signUp() {
        Map<String, String> param = new HashMap<>();
        String userId = "jy";
        String password = "123";
        String name = "jiyeon";
        String email = "jy@abc.com";

        param.put("userId", userId);
        param.put("password", password);
        param.put("name", name);
        param.put("email", email);

        userController.doGet(param, null);

        User user = DataBase.findUserById(userId);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }
}

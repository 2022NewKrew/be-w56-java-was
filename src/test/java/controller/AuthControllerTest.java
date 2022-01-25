package controller;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class AuthControllerTest {

    @Test
    void signUp() {
        String userId = "jy";
        String password = "123";
        String name = "jiyeon";
        String email = "jy@abc.com";

        AuthController.signUp(userId, password, name, email);

        User user = DataBase.findUserById(userId);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }
}

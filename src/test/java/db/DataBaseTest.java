package db;

import model.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseTest {

    @Test
    void addUserTest() {
        User user = new User("test", "test", "test", "test@kakao.com");
        DataBase.addUser(user);
    }

    @Test
    void findUserByIdTest() {
        DataBase.findUserById("test");
    }

    @Test
    void findAllTest() {
        DataBase.findAll();
    }
}

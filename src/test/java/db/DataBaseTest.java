package db;

import model.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseTest {

    @Test
    void add() {
        try {
            User user = new User("test", "test", "test", "test@kakao.com");
            DataBase.add(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

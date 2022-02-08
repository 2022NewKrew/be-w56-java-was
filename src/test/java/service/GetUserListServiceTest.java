package service;

import static org.assertj.core.api.Assertions.assertThat;

import db.DataBase;
import java.util.List;
import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GetUserListServiceTest {

    private static final User[] users = {
        new User("champ.ion", "test", "champ", "champ@kakao.com"),
        new User("kakaofriends", "tset", "kakao", "kakaofirends@kakao.com")
    };

    @BeforeAll
    static void setup() {
        for (User user : users) {
            DataBase.addUser(user);
        }
    }

    @AfterAll
    static void close() {
        DataBase.clear();
    }

    @Test
    void getUserList() {
        List<User> userList = GetUserListService.getUserList();
        for (User user : userList) {
            assertThat(user).isIn((Object[]) users);
        }
    }
}

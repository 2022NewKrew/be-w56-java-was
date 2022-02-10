package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import db.DataBase;
import db.UserStorage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class GetUserListServiceTest {

    private final User[] users = {
        new User("champ.ion", "test", "champ", "champ@kakao.com"),
        new User("kakaofriends", "tset", "kakao", "kakaofirends@kakao.com")
    };

    private static MockedStatic<UserStorage> mUserStorage;

    @BeforeAll
    public static void setup() {
        mUserStorage = mockStatic(UserStorage.class);
    }

    @AfterAll
    static void close() {
        mUserStorage.close();
    }

    @Test
    void getUserList() throws SQLException, ClassNotFoundException {
        when(UserStorage.findAll()).thenReturn(new ArrayList<>(List.of(users)));

        List<User> userList = GetUserListService.getUserList();
        for (User user : userList) {
            assertThat(user).isIn((Object[]) users);
        }
    }
}

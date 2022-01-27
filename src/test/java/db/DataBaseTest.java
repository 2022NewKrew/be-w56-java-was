package db;

import model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

class DataBaseTest {
    @Test
    public void addUser() {
        try {
            User user = new User("test", "1234", "테스트", "test@email.com");
            DataBase.addUser(user);
            Field field = DataBase.class.getDeclaredField("users");
            field.setAccessible(true);

            Map<String, User> users = (Map<String, User>) field.get(DataBase.class);
            User createdUser = users.get("test");
            assertThat(user.getUserId()).isEqualTo(createdUser.getUserId());
            assertThat(user.getPassword()).isEqualTo(createdUser.getPassword());
            assertThat(user.getName()).isEqualTo(createdUser.getName());
            assertThat(user.getEmail()).isEqualTo(createdUser.getEmail());

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findUserById() {

        User user = new User("testFind", "1234", "테스트", "test@email.com");
        DataBase.addUser(user);
        User findUser = DataBase.findUserById("testFind");

        assertThat(user.getUserId()).isEqualTo(findUser.getUserId());
        assertThat(user.getPassword()).isEqualTo(findUser.getPassword());
        assertThat(user.getName()).isEqualTo(findUser.getName());
        assertThat(user.getEmail()).isEqualTo(findUser.getEmail());

    }

    @Test
    public void findUserById_userNotExist() {
        assertThat(DataBase.findUserById("testFindUserNotExist")).isNull();
    }

    @Test
    public void findAll() {
        User user1 = new User("test1", "1", "테스트1", "test1@email.com");
        DataBase.addUser(user1);
        User user2 = new User("test2", "2", "테스트2", "test2@email.com");
        DataBase.addUser(user2);
        User user3 = new User("test3", "3", "테스트3", "test3@email.com");
        DataBase.addUser(user3);

        Collection<User> users = DataBase.findAll();

        assertThat(users).contains(user1);
        assertThat(users).contains(user2);
        assertThat(users).contains(user3);
    }
}
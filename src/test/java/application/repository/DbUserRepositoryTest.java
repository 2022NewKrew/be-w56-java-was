package application.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrderer.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import application.model.User;

class DbUserRepositoryTest {

    @Test
    @DisplayName("DB 접속 테스트")
    void test() throws Exception {
        // given
        User user = new User("wctss", "1234", "aiden2", "asodfj@asfod.com");
        UserRepository repository = new DbUserRepository();
        repository.addUser(user);
        // when

        // then

    }
    @Test
    @DisplayName("유저 찾기 테스트")
    void test_1() throws Exception {
        // given
        UserRepository repository = new DbUserRepository();
        User wcts = repository.findUserById("wcts");
        System.out.println("wcts = " + wcts);

        // when

        // then

    }
    @Test
    @DisplayName("길이가 0인 배열의 lenght 확인")
    void test_2() throws Exception {
        // given
        String[] str = {};
        System.out.println("str.length = " + str.length);

        // when

        // then

    }

    @Test
    @DisplayName("Users 찾기 테스트")
    void test_3() throws Exception {
        // given
        UserRepository repository = new DbUserRepository();
        List<User> all = repository.findAll();
        for (User user : all) {
            System.out.println("user = " + user);
        }
    }

}

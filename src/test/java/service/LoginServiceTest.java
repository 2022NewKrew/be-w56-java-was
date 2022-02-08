package service;

import static org.assertj.core.api.Assertions.assertThat;

import db.DataBase;
import model.LoginRequest;
import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginServiceTest {

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

    @DisplayName("로그인 성공 테스트")
    @Test
    void loginSuccess() {
        LoginRequest loginRequest = new LoginRequest("champ.ion", "test");
        assertThat(LoginService.login(loginRequest)).isTrue();
    }

    @DisplayName("비밀번호 불일치 테스트")
    @Test
    void loginWithWrongPassword() {
        LoginRequest loginRequest = new LoginRequest("champ.ion", "tset");
        assertThat(LoginService.login(loginRequest)).isFalse();
    }

    @DisplayName("존재하지 않는 사용자 테스트")
    @Test
    void loginWithNotExistUser() {
        LoginRequest loginRequest = new LoginRequest("champ", "test");
        assertThat(LoginService.login(loginRequest)).isFalse();
    }
}

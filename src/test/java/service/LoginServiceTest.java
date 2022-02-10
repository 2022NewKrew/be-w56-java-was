package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static service.LoginService.LOGIN_FAILED;

import db.UserStorage;
import java.sql.SQLException;
import model.LoginRequest;
import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class LoginServiceTest {

    private final User givenUser = new User("champ.ion", "test", "champ", "champ@kakao.com");

    private static MockedStatic<UserStorage> mUserStorage;

    @BeforeAll
    public static void setup() {
        mUserStorage = mockStatic(UserStorage.class);
    }

    @AfterAll
    public static void close() {
        mUserStorage.close();
    }

    @DisplayName("로그인 성공 테스트")
    @Test
    void loginSuccess() throws SQLException, ClassNotFoundException {
        String userId = "champ.ion";
        String password = "test";
        LoginRequest loginRequest = new LoginRequest(userId, password);
        when(UserStorage.findUserById(userId)).thenReturn(givenUser);
        assertThat(LoginService.login(loginRequest)).isEqualTo(userId.hashCode());
    }

    @DisplayName("비밀번호 불일치 테스트")
    @Test
    void loginWithWrongPassword() throws SQLException, ClassNotFoundException {
        String userId = "champ.ion";
        String password = "tset";
        LoginRequest loginRequest = new LoginRequest(userId, password);
        when(UserStorage.findUserById(userId)).thenReturn(givenUser);
        assertThat(LoginService.login(loginRequest)).isEqualTo(LOGIN_FAILED);
    }

    @DisplayName("존재하지 않는 사용자 테스트")
    @Test
    void loginWithNotExistUser() throws SQLException, ClassNotFoundException {
        String userId = "champ";
        String password = "test";
        LoginRequest loginRequest = new LoginRequest(userId, password);
        when(UserStorage.findUserById(userId)).thenReturn(null);
        assertThat(LoginService.login(loginRequest)).isEqualTo(LOGIN_FAILED);
    }
}

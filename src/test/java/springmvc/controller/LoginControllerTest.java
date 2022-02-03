package springmvc.controller;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import springmvc.db.DataBase;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class LoginControllerTest {

    LoginController loginController = new LoginController();

    @BeforeEach
    void setUp() {
        String userId = "jy";
        String password = "123";
        String name = "jiyeon";
        String email = "jy@abc.com";
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
    }

    @Nested
    @DisplayName("로그인 테스트")
    class login {
        @Test
        @DisplayName("로그인 성공")
        void loginSuccess() {
            //Given
            Map<String, String> param = new HashMap<>();
            Map<String, String> body = new HashMap<>();
            Map<String, String> sessionCookie = new HashMap<>();
            body.put("userId", "jy");
            body.put("password", "123");

            //When
            String viewName = loginController.doPost(param, body, sessionCookie);

            //Then
            assertThat(sessionCookie.get("logined")).isEqualTo("true");
            assertThat(viewName).isEqualTo("redirect:/index.html");
        }

        @Test
        @DisplayName("잘못된 아이디로 로그인 실패")
        void loginFailedWithWrongUserId() {
            //Given
            Map<String, String> param = new HashMap<>();
            Map<String, String> body = new HashMap<>();
            Map<String, String> sessionCookie = new HashMap<>();
            body.put("userId", "wrong");
            body.put("password", "123");

            //When
            String viewName = loginController.doPost(param, body, sessionCookie);

            //Then
            assertThat(sessionCookie.get("logined")).isEqualTo("false");
            assertThat(viewName).isEqualTo("redirect:/user/login_failed.html");
        }

        @Test
        @DisplayName("잘못된 패스워드로 로그인 실패")
        void loginFailedWithWrongPassword() {
            //Given
            Map<String, String> param = new HashMap<>();
            Map<String, String> body = new HashMap<>();
            Map<String, String> sessionCookie = new HashMap<>();
            body.put("userId", "jy");
            body.put("password", "wrong");

            //When
            String viewName = loginController.doPost(param, body, sessionCookie);

            //Then
            assertThat(sessionCookie.get("logined")).isEqualTo("false");
            assertThat(viewName).isEqualTo("redirect:/user/login_failed.html");
        }
    }


    @Test
    @DisplayName("로그인 상태에서 접근 시 index 페이지로 리다이렉트")
    void loginWithWrongPassword() {
        //Given
        Map<String, String> param = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        Map<String, String> sessionCookie = new HashMap<>();
        sessionCookie.put("logined", "true");

        //When
        String viewName = loginController.doGet(param, sessionCookie);

        //Then
        assertThat(viewName).isEqualTo("redirect:/index.html");
    }

}

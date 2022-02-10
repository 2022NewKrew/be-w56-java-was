package springmvc.controller;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import springmvc.db.DataBase;
import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatus;

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
            Map<String, String> header = new HashMap<>();
            Map<String, String> body = new HashMap<>();
            body.put("userId", "jy");
            body.put("password", "123");
            HttpRequest httpRequest = new HttpRequest(HttpMethod.POST, "/login", "HTTP/1.1", header, body);
            HttpResponse httpResponse = new HttpResponse();

            //When
            String viewName = loginController.doPost(httpRequest, httpResponse).getView();

            //Then
            assertThat(httpResponse.getCookies().get("logined")).isEqualTo("true");
            assertThat(viewName).isEqualTo("/");
        }

        @Test
        @DisplayName("잘못된 아이디로 로그인 실패")
        void loginFailedWithWrongUserId() {
            //Given
            Map<String, String> header = new HashMap<>();
            Map<String, String> body = new HashMap<>();
            body.put("userId", "wrong");
            body.put("password", "123");
            HttpRequest httpRequest = new HttpRequest(HttpMethod.POST, "/login", "HTTP/1.1", header, body);
            HttpResponse httpResponse = new HttpResponse();

            //When
            String viewName = loginController.doPost(httpRequest, httpResponse).getView();

            //Then
            assertThat(httpResponse.getCookies().get("logined")).isEqualTo("false");
            assertThat(viewName).isEqualTo("/user/login_failed.html");
        }

        @Test
        @DisplayName("잘못된 패스워드로 로그인 실패")
        void loginFailedWithWrongPassword() {
            //Given
            Map<String, String> header = new HashMap<>();
            Map<String, String> body = new HashMap<>();
            body.put("userId", "jy");
            body.put("password", "wrong");
            HttpRequest httpRequest = new HttpRequest(HttpMethod.POST, "/login", "HTTP/1.1", header, body);
            HttpResponse httpResponse = new HttpResponse();

            //When
            String viewName = loginController.doPost(httpRequest, httpResponse).getView();

            //Then
            assertThat(httpResponse.getCookies().get("logined")).isEqualTo("false");
            assertThat(viewName).isEqualTo("/user/login_failed.html");
        }
    }


    @Test
    @DisplayName("로그인 상태에서 접근 시 index 페이지로 리다이렉트")
    void loginWithWrongPassword() {
        //Given
        Map<String, String> header = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        header.put("Cookie", "logined=true");
        HttpRequest httpRequest = new HttpRequest(HttpMethod.POST, "/login", "HTTP/1.1", header, body);
        HttpResponse httpResponse = new HttpResponse();

        //When
        String viewName = loginController.doGet(httpRequest, httpResponse).getView();

        //Then
        assertThat(viewName).isEqualTo("/index.html");
    }

}

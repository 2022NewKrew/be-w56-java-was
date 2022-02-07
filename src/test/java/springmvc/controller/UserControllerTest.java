package springmvc.controller;

import org.junit.jupiter.api.DisplayName;
import springmvc.db.DataBase;
import model.User;
import org.junit.jupiter.api.Test;
import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class UserControllerTest {

    UserController userController = new UserController();

    @Test
    @DisplayName("GET 회원가입 성공")
    void signUpGet() {
        String userId = "jy";
        String password = "123";
        String name = "jiyeon";
        String email = "jy@abc.com";

        Map<String, String> param = new HashMap<>();
        param.put("userId", userId);
        param.put("password", password);
        param.put("name", name);
        param.put("email", email);

        HttpRequest httpRequest = new HttpRequest(HttpMethod.POST, "/user", "HTTP/1.1", param);
        HttpResponse httpResponse = new HttpResponse();

        userController.doGet(httpRequest, httpResponse);

        User user = DataBase.findUserById(userId).get();
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("POST 회원가입 성공")
    void signUpPost() {

        // Given
        Map<String, String> header = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        String userId = "jy";
        String password = "123";
        String name = "jiyeon";
        String email = "jy@abc.com";
        body.put("userId", userId);
        body.put("password", password);
        body.put("name", name);
        body.put("email", email);
        HttpRequest httpRequest = new HttpRequest(HttpMethod.POST, "/user", "HTTP/1.1", header, body);
        HttpResponse httpResponse = new HttpResponse();

        // When
        String viewName = userController.doPost(httpRequest, httpResponse);

        // Then
        User user = DataBase.findUserById(userId).get();
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(viewName).isEqualTo("redirect:/index.html");
    }

    @Test
    @DisplayName("아이디 중복으로 회원가입 실패")
    void signUpFailedWithDuplicateUser() {

        //Given
        String userId = "jy";
        String password = "123";
        String name = "jiyeon";
        String email = "jy@abc.com";
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);

        Map<String, String> header = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        body.put("userId", userId);
        body.put("password", "1233");
        body.put("name", "jiyeon1");
        body.put("email", "jy1@abc.com");
        HttpRequest httpRequest = new HttpRequest(HttpMethod.POST, "/user", "HTTP/1.1", header, body);
        HttpResponse httpResponse = new HttpResponse();

        //When
        String viewName = userController.doPost(httpRequest, httpResponse);

        //Then
        assertThat(viewName).isEqualTo("redirect:/user/form_failed.html");
    }
}

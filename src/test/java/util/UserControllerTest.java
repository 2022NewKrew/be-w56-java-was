package util;

import app.controller.UserController;
import app.db.DataBase;
import app.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.http.HttpRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp(){
        this.userController = new UserController();
    }

    @Test
    void signInByGetTest(){
        HttpRequest httpRequest = new HttpRequest(
                "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n");
        userController.signInByGet(httpRequest.params());
        User user = DataBase.findUserById("javajigi");
        assertThat(
                        (user.getEmail().equals("javajigi@slipp.net")) &&
                        (user.getPassword().equals("password")) &&
                        (user.getName().equals("박재성"))
        ).isTrue();
    }

}

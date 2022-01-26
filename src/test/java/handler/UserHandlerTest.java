package handler;

import db.DataBase;
import http.Request;
import http.Response;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserHandlerTest {

    private UserHandler subject;

    @BeforeEach
    void setUp() {
        subject = new UserHandler();
    }

    @Test
    void create() {
        String body = "userId=javajigi&password=password&name=자바지기&email=javajigi@slipp.net\n";
        Request request = Request.newBuilder().body(body).build();

        Response result = subject.create(request);

        assertEquals(301, result.getStatusCode());
        assertEquals("/user/profile.html", result.getHeader("Location"));
    }

    @Test
    void login() {
        DataBase.addUser(new User("javajigi", "password", "자바지기", "email@example.com"));
        String body = "userId=javajigi&password=password\n";
        Request request = Request.newBuilder().body(body).build();

        Response result = subject.login(request);

        assertEquals(301, result.getStatusCode());
        assertEquals("/index.html", result.getHeader("Location"));
        assertEquals("loggedIn=true; path=/", result.getHeader("Set-Cookie"));
    }

    @Test
    void login_failed() {
        DataBase.addUser(new User("javajigi", "password", "자바지기", "email@example.com"));
        String body = "userId=javajigi&password=wrongPassword\n";
        Request request = Request.newBuilder().body(body).build();

        Response result = subject.login(request);

        assertEquals(301, result.getStatusCode());
        assertEquals("/user/login_failed.html", result.getHeader("Location"));
        assertEquals("loggedIn=false; path=/", result.getHeader("Set-Cookie"));
    }
}

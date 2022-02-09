package app.handler;

import domain.model.User;
import lib.was.http.Request;
import lib.was.http.Response;
import lib.was.template.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserHandlerTest {

    private UserService service;
    private UserHandler subject;

    @BeforeEach
    void setUp() {
        service = mock(UserService.class);
        TemplateEngine templateEngine = new TemplateEngine();
        subject = new UserHandler(service, templateEngine);
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
        User user = new User(0, "javajigi", "password", "자바지기", "email@example.com");
        when(service.login(anyString(), anyString())).thenReturn(Optional.of(user));
        String body = "userId=javajigi&password=password\n";
        Request request = Request.newBuilder().body(body).build();

        Response result = subject.login(request);

        assertEquals(301, result.getStatusCode());
        assertEquals("/index.html", result.getHeader("Location"));
        assertEquals("loggedIn=true; path=/", result.getHeader("Set-Cookie"));
    }

    @Test
    void login_failed() {
        when(service.login(anyString(), anyString())).thenReturn(Optional.empty());
        String body = "userId=javajigi&password=wrongPassword\n";
        Request request = Request.newBuilder().body(body).build();

        Response result = subject.login(request);

        assertEquals(301, result.getStatusCode());
        assertEquals("/user/login_failed.html", result.getHeader("Location"));
        assertEquals("loggedIn=false; path=/", result.getHeader("Set-Cookie"));
    }
}

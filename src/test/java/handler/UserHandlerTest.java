package handler;

import http.Request;
import http.Response;
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
}

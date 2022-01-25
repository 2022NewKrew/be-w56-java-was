package handler;

import http.Locator;
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
        Locator locator = Locator.parse("?name=test&password=test&email=foo@example.com&userId=foobar");
        Request request = Request.newBuilder().locator(locator).build();

        Response result = subject.create(request);

        assertEquals(301, result.getStatusCode());
        assertEquals("/user/profile.html", result.getHeader("Location"));
    }
}

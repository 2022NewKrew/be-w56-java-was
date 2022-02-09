package app.handler;

import domain.model.Post;
import domain.model.User;
import lib.was.http.Headers;
import lib.was.http.Request;
import lib.was.http.Response;
import lib.was.template.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.PostService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostHandlerTest {

    private PostService service;
    private PostHandler subject;

    @BeforeEach
    void setUp() {
        service = mock(PostService.class);
        TemplateEngine templateEngine = new TemplateEngine();
        subject = new PostHandler(service, templateEngine);
    }

    @Test
    void create() {
        String body = "title=foo&contents=bar\n";
        Headers headers = new Headers(Collections.singletonMap("Cookie", "currentUserId=1"));
        Request request = Request.newBuilder()
                .headers(headers)
                .body(body)
                .build();

        Response result = subject.create(request);

        assertEquals(302, result.getStatusCode());
        assertEquals("/", result.getHeader("Location"));
    }

    @Test
    void create_notLoggedIn() {
        String body = "title=foo&contents=bar&writer=1\n";
        Request request = Request.newBuilder()
                .headers(new Headers(Collections.emptyMap()))
                .body(body)
                .build();

        Response result = subject.create(request);

        assertEquals(302, result.getStatusCode());
        assertEquals("/user/login.html", result.getHeader("Location"));
    }

    @Test
    void list() {
        User user = new User(0, "javajigi", "password", "자바지기", "email@example.com");
        Post post = new Post.Builder()
                .author(user)
                .title("foo")
                .content("bar")
                .createdAt(LocalDateTime.now())
                .build();
        when(service.findAllPosts()).thenReturn(List.of(post));
        Request request = Request.newBuilder().build();

        Response result = subject.list(request);

        assertEquals(200, result.getStatusCode());
        assertTrue(new String(result.getBody()).contains("foo"));
    }
}

package app.handler;

import domain.model.Post;
import domain.model.User;
import lib.util.HttpRequestUtils;
import lib.was.di.Bean;
import lib.was.di.Inject;
import lib.was.http.ContentType;
import lib.was.http.Headers;
import lib.was.http.Request;
import lib.was.http.Response;
import lib.was.template.TemplateEngine;
import service.PostService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Bean
public class PostHandler {

    private final PostService service;
    private final TemplateEngine templateEngine;

    @Inject
    public PostHandler(PostService service, TemplateEngine templateEngine) {
        this.service = service;
        this.templateEngine = templateEngine;
    }

    public Response create(Request request) {
        String cookieHeader = request.getHeader("Cookie");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieHeader);
        boolean loggedIn = cookies.containsKey("currentUserId");
        if (!loggedIn) {
            Map<String, String> headers = Map.of(
                    "Content-Type", ContentType.TEXT.getContentType(),
                    "Location", "/user/login.html"
            );
            return Response.of(302, new Headers(headers), "Not logged in");
        }
        String body = request.getBody();
        Map<String, String> params = HttpRequestUtils.parseQueryString(body);
        String title = params.get("title");
        String content = params.get("contents");
        long authorId = Long.parseLong(cookies.get("currentUserId"));
        Post post = new Post.Builder()
                .title(title)
                .content(content)
                .author(new User(authorId, "", "", "", ""))
                .build();
        service.addPost(post);
        Map<String, String> headers = Map.of(
                "Content-Type", "text/plain",
                "Location", "/"
        );
        return Response.of(302, new Headers(headers), "");
    }

    public Response list(Request request) {
        File file = new File("./webapp/index.html");
        try {
            String content = Files.readString(file.toPath());
            Map<String, Object> values = Map.of(
                    "posts", service.findAllPosts()
            );
            String filled = templateEngine.render(content, values);
            return Response.ok(Headers.contentType(ContentType.HTML), filled);
        } catch (IOException e) {
            return Response.error(Headers.contentType(ContentType.TEXT), e.getMessage());
        }
    }
}

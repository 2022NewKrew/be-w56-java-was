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
        Long currentUserId = request.getCookieLong("currentUserId");
        if (currentUserId == null) {
            return Response.found("Not logged in")
                    .contentType(ContentType.HTML)
                    .redirect("/user/login.html");
        }
        String title = request.getBodyParam("title");
        String content = request.getBodyParam("contents");
        Post post = new Post.Builder()
                .title(title)
                .content(content)
                .author(new User(currentUserId, "", "", "", ""))
                .build();
        service.addPost(post);
        return Response.found("")
                .contentType(ContentType.HTML)
                .redirect("/");
    }

    public Response list(Request request) {
        File file = new File("./webapp/index.html");
        try {
            String content = Files.readString(file.toPath());
            Map<String, Object> values = Map.of(
                    "posts", service.findAllPosts()
            );
            String filled = templateEngine.render(content, values);
            return Response.ok(filled).contentType(ContentType.HTML);
        } catch (IOException e) {
            return Response.error(e.getMessage()).contentType(ContentType.HTML);
        }
    }
}

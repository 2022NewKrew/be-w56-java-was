package app.handler;

import domain.model.User;
import lib.was.di.Bean;
import lib.was.di.Inject;
import lib.was.http.ContentType;
import lib.was.http.Request;
import lib.was.http.Response;
import lib.was.template.TemplateEngine;
import service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

@Bean
public class UserHandler {

    private final UserService service;
    private final TemplateEngine templateEngine;

    @Inject
    public UserHandler(UserService service, TemplateEngine templateEngine) {
        this.service = service;
        this.templateEngine = templateEngine;
    }

    public Response create(Request request) {
        String userId = request.getBodyParam("userId");
        String password = request.getBodyParam("password");
        String email = request.getBodyParam("email");
        String name = request.getBodyParam("name");
        User user = new User(0, userId, password, name, email);
        service.addUser(user);
        return Response.found("")
                .contentType(ContentType.TEXT)
                .redirect("/user/profile.html");
    }

    public Response login(Request request) {
        String userId = request.getBodyParam("userId");
        String password = request.getBodyParam("password");
        Optional<User> user = service.login(userId, password);
        if (user.isEmpty()) {
            return Response.found("Login failed")
                    .contentType(ContentType.TEXT)
                    .redirect("/user/login_failed.html");
        }
        return Response.found("Login success")
                .contentType(ContentType.TEXT)
                .redirect("/")
                .setCookie("currentUserId", String.valueOf(user.get().getId()))
                .setCookie("path", "/");
    }

    public Response list(Request request) {
        Long currentUserId = request.getCookieLong("currentUserId");
        if (currentUserId == null) {
            return Response.found("Not logged in")
                    .contentType(ContentType.TEXT)
                    .redirect("/user/login.html");
        }
        File file = new File("./webapp/user/list.html");
        try {
            String content = Files.readString(file.toPath());
            Map<String, Object> values = Map.of(
                    "users", service.findAllUsers()
            );
            String filled = templateEngine.render(content, values);
            return Response.ok(filled).contentType(ContentType.HTML);
        } catch (IOException e) {
            return Response.error(e.getMessage()).contentType(ContentType.TEXT);
        }
    }
}

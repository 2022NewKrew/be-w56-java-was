package app.handler;

import lib.was.di.Bean;
import lib.was.di.Inject;
import app.db.Database;
import lib.was.http.ContentType;
import lib.was.http.Headers;
import lib.was.http.Request;
import lib.was.http.Response;
import domain.model.User;
import lib.was.template.TemplateEngine;
import lib.util.HttpRequestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Bean
public class UserHandler {

    private final Database database;
    private final TemplateEngine templateEngine;

    @Inject
    public UserHandler(Database database, TemplateEngine templateEngine) {
        this.database = database;
        this.templateEngine = templateEngine;
    }

    public Response create(Request request) {
        String body = request.getBody();
        Map<String, String> params = HttpRequestUtils.parseQueryString(body);
        String userId = params.get("userId");
        String password = params.get("password");
        String email = params.get("email");
        String name = params.get("name");
        User user = new User(0, userId, password, name, email);
        database.addUser(user);
        Map<String, String> headers = Map.of(
                "Content-Type", "text/plain",
                "Location", "/user/profile.html"
        );
        return Response.of(301, new Headers(headers), "");
    }

    public Response login(Request request) {
        String body = request.getBody();
        Map<String, String> params = HttpRequestUtils.parseQueryString(body);
        String userId = params.get("userId");
        String password = params.get("password");
        User user = database.findUserById(userId);
        if (user == null || !user.getPassword().equals(password)) {
            var headers = Map.of(
                    "Content-Type", ContentType.TEXT.getContentType(),
                    "Location", "/user/login_failed.html",
                    "Set-Cookie", "loggedIn=false; path=/"
            );
            return Response.of(301, new Headers(headers), "Login failed");
        }
        Map<String, String> headers = Map.of(
                "Content-Type", ContentType.TEXT.getContentType(),
                "Location", "/index.html",
                "Set-Cookie", "loggedIn=true; path=/"
        );
        return Response.of(301, new Headers(headers), "Login success");
    }

    public Response list(Request request) {
        String cookieHeader = request.getHeader("Cookie");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieHeader);
        boolean loggedIn = Boolean.parseBoolean(cookies.get("loggedIn"));
        if (!loggedIn) {
            Map<String, String> headers = Map.of(
                    "Content-Type", ContentType.TEXT.getContentType(),
                    "Location", "/user/login.html"
            );
            return Response.of(302, new Headers(headers), "Not logged in");
        }
        File file = new File("./webapp/user/list.html");
        try {
            String content = Files.readString(file.toPath());
            Map<String, Object> values = Map.of(
                    "users", database.findAllUsers()
            );
            String filled = templateEngine.render(content, values);
            return Response.ok(Headers.contentType(ContentType.HTML), filled);
        } catch (IOException e) {
            return Response.error(Headers.contentType(ContentType.TEXT), e.getMessage());
        }
    }
}

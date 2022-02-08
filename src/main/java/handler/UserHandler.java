package handler;

import annotation.Bean;
import annotation.Inject;
import db.Database;
import http.ContentType;
import http.Headers;
import http.Request;
import http.Response;
import model.User;
import template.TemplateEngine;
import util.HttpRequestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Bean
public class UserHandler {

    private final Database database;

    @Inject
    public UserHandler(Database database) {
        this.database = database;
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
            String filled = new TemplateEngine().render(content, values);
            return Response.ok(Headers.contentType(ContentType.HTML), filled);
        } catch (IOException e) {
            return Response.error(Headers.contentType(ContentType.TEXT), e.getMessage());
        }
    }
}

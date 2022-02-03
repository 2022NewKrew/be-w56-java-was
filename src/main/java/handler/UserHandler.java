package handler;

import annotation.Bean;
import db.DataBase;
import http.ContentType;
import http.Headers;
import http.Request;
import http.Response;
import model.User;
import util.HttpRequestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Bean
public class UserHandler {

    public Response create(Request request) {
        String body = request.getBody();
        Map<String, String> params = HttpRequestUtils.parseQueryString(body);
        String userId = params.get("userId");
        String password = params.get("password");
        String email = params.get("email");
        String name = params.get("name");
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
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
        User user = DataBase.findUserById(userId);
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
            String regex = "\\{\\{#users}}(.+)\\{\\{/users}}";
            Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
            Matcher matcher = pattern.matcher(content);
            //noinspection ResultOfMethodCallIgnored
            matcher.find();
            String found = matcher.group(1);
            AtomicInteger i = new AtomicInteger(0);
            String section = DataBase.findAll()
                    .stream()
                    .map(user -> found.replaceAll("\\{\\{id}}", String.valueOf(i.incrementAndGet()))
                            .replaceAll("\\{\\{name}}", user.getName())
                            .replaceAll("\\{\\{userId}}", user.getUserId())
                            .replaceAll("\\{\\{email}}", user.getEmail()))
                    .collect(Collectors.joining("\n"));
            String filled = matcher.replaceAll(section);
            return Response.ok(Headers.contentType(ContentType.HTML), filled);
        } catch (IOException e) {
            return Response.error(Headers.contentType(ContentType.TEXT), e.getMessage());
        }
    }
}

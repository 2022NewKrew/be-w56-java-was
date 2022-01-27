package handler;

import annotation.Bean;
import db.DataBase;
import http.ContentType;
import http.Headers;
import http.Request;
import http.Response;
import model.User;
import util.HttpRequestUtils;

import java.util.Map;

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
}

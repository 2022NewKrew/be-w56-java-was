package handler;

import db.DataBase;
import http.Headers;
import http.Request;
import http.Response;
import model.User;
import util.HttpRequestUtils;

import java.util.Map;

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
}

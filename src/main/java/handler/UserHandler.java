package handler;

import db.DataBase;
import http.Headers;
import http.Request;
import http.Response;
import model.User;

import java.util.Map;

public class UserHandler {

    public Response create(Request request) {
        Map<String, String> queryParams = request.getQueryParams();
        String userId = queryParams.get("userId");
        String password = queryParams.get("password");
        String email = queryParams.get("email");
        String name = queryParams.get("name");
        User user = new User(userId, password, name, email);
        DataBase.addUser(user);
        Map<String, String> headers = Map.of(
                "Content-Type", "text/plain",
                "Location", "/user/profile.html"
        );
        return Response.of(301, new Headers(headers), "");
    }
}

package controller;

import http.Request;
import http.Response;
import model.User;
import service.UserService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JoinController implements Controller{

    @Override
    public void makeResponse(Request request, Response response) {
        Map<String, String> newUser = request.getBody();
        String userId = newUser.get("userId");
        String password = newUser.get("password");
        String name = newUser.get("name");
        String email = URLDecoder.decode(newUser.get("email"), StandardCharsets.UTF_8);

        User user = new User(userId, password, name, email);
        UserService.join(user);
        response.redirectResponse("/index.html");
    }
}

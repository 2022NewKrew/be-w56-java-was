package controller;

import http.Request;
import http.Response;
import model.User;
import service.UserService;
import java.util.Map;

public class JoinController implements Controller{

    @Override
    public void makeResponse(Request request, Response response) {
        Map<String, String> newUser = request.getBody();
        User user = new User(newUser.get("userId"), newUser.get("password"), newUser.get("name"), newUser.get("email"));
        UserService.join(user);
        response.redirectResponse("/index.html");
    }
}

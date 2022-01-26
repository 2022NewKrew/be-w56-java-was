package controller;

import http.Request;
import http.Response;
import model.User;
import service.UserService;
import java.util.Map;

public class UserController implements Controller{

    private final UserService userService = new UserService();

    @Override
    public void makeResponse(Request request, Response response) {
        createUser(request);
        response.redirectResponse("/index.html");
    }

    private void createUser(Request request) {
        Map<String, String> newUser = request.getBody();
        User user = new User(newUser.get("userId"), newUser.get("password"), newUser.get("name"), newUser.get("email"));
        userService.join(user);
    }



}

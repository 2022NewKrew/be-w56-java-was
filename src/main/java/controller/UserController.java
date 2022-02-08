package controller;

import model.Request;
import model.Response;
import model.User;
import service.UserService;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class UserController extends AbstractController {
    private static final String INDEX_URL = "/index.html";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(Request request, Response response) throws IOException {
        userService.save(request.getQueryStrings());

        response.set302Header(INDEX_URL);
    }

    @Override
    protected void doPost(Request request, Response response) throws IOException {
        String name = request.getRequestBody("name");
        String password = request.getRequestBody("password");
        String userId = request.getRequestBody("userId");
        String email = URLDecoder.decode(request.getRequestBody("email"), StandardCharsets.UTF_8);

        User user = new User(userId, password, name, email);
        userService.save(user);

        response.set302Header(INDEX_URL);
    }
}

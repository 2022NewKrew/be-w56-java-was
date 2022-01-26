package controller;

import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import httpmodel.HttpSession;
import httpmodel.HttpSessions;
import java.io.IOException;
import model.User;
import service.UserService;
import util.FileConverter;

public class LoginController extends AbstractController {

    private static final String INDEX_HTML = "/index.html";

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (userService.isLogin(httpRequest)) {
            httpResponse.set302Found(INDEX_HTML);
            return;
        }
        byte[] responseBody = FileConverter.fileToString("/user/login.html");
        httpResponse.set200OK(httpRequest, responseBody);
    }

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        String account = httpRequest.getRequestBody("userId");
        String password = httpRequest.getRequestBody("password");
        String name = httpRequest.getRequestBody("name");
        String email = httpRequest.getRequestBody("email");
        User user = userService.login(new User(account, password, name, email));

        addSession(httpRequest, user);

        httpResponse.set302Found(INDEX_HTML);
        httpResponse.addHeader("Location", INDEX_HTML);
    }

    private void addSession(HttpRequest request, User user) {
        HttpSession httpSession = request.getHttpSession();
        httpSession.setAttribute(httpSession.getId(), user);
        HttpSessions.add(httpSession.getId(), httpSession);
    }
}

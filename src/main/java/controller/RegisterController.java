package controller;

import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import httpmodel.HttpSession;
import httpmodel.HttpSessions;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import model.User;
import service.UserService;
import util.FileConverter;

public class RegisterController extends AbstractController {

    private static final String INDEX_HTML = "/index.html";
    private static final String USER = "user";

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        HttpSession httpSession = httpRequest.getHttpSession();
        if (Objects.nonNull(httpSession.getAttribute(USER))) {
            httpResponse.set302Found(INDEX_HTML);
            return;
        }
        byte[] responseBody = FileConverter.fileToString("/user/form.html");
        httpResponse.set200OK(httpRequest, responseBody);
    }

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        String account = httpRequest.getRequestBody("userId");
        String password = httpRequest.getRequestBody("password");
        String name = httpRequest.getRequestBody("name");
        String email = URLDecoder.decode(httpRequest.getRequestBody("email"),
            StandardCharsets.UTF_8);
        User user = new User(account, password, name, email);
        userService.save(user);

        addSession(httpRequest, user);
        httpResponse.addCookie(httpRequest);
        httpResponse.set302Found(INDEX_HTML);
    }

    private void addSession(HttpRequest httpRequest, User user) {
        HttpSession httpSession = httpRequest.getHttpSession();
        httpSession.setAttribute(USER, user);
        HttpSessions.add(httpSession.getId(), httpSession);
    }
}

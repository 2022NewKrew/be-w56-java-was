package controller;

import http.HttpRequest;
import http.HttpResponse;
import service.UserService;
import util.HttpRequestUtils;
import view.UserView;
import webserver.HttpMethod;

import java.io.IOException;
import java.util.Map;

public class ShowUsersController implements Controller {

    private final UserService userService = new UserService();

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        HttpMethod httpMethod = request.getRequestLine().getHttpMethod();

        if (httpMethod.isGET()) {
            doGet(request, response);
        }
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, String> header = request.getHttpHeader().getHeader();

        if (!isLogin(header.get("Cookie"))) {
            response.sendRedirect302Header("/users/login");
            return;
        }

        String allUserAsString = UserView.getAllUserAsHTML(userService.findAllUser());
        response.forwardBody(allUserAsString);
    }

    private boolean isLogin(String cookieValue) {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieValue);
        String value = cookies.get("logined");
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }
}

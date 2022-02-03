package controller;

import java.util.Map;

import http.Request;
import http.Response;
import service.UserService;

public class UserController implements Controller {
    private final String CREATE_USER_URI = "/users/create(.*)";
    private final String LOGIN_USER_URI = "/users/login(.*)";

    private final String INDEX_HTML = "/index.html";
    private final String LOGIN_HTML = "/user/login.html";
    private final String CREATE_HTML = "/user/form.html";

    private final String LOGIN_SUCCESS_COOKIE = "logined=true; Path=/";

    private final String GET_METHOD = "GET";
    private final String POST_METHOD = "POST";

    private final UserService userService;

    public UserController() {
        userService = new UserService();
    }

    public String run(Request request, Response response) {
        if (request.getRequestMethod().equals(POST_METHOD)) {
            return postHandler(request, response);
        }

        if (request.getRequestMethod().equals(GET_METHOD)) {
            return getHandler();
        }

        return INDEX_HTML;
    }

    private String postHandler(Request request, Response response) {
        String url = request.getRequestUrl();
        Map<String, String> parameters = request.getParameters();
        if (url.matches(CREATE_USER_URI)) {
            return create(parameters);
        }

        if (url.matches(LOGIN_USER_URI)) {
            return login(parameters, response);
        }

        return INDEX_HTML;
    }

    private String getHandler() {
        return INDEX_HTML;
    }

    private String login(Map<String, String> parameters, Response response) {
        if (!userService.login(parameters)) {
            return LOGIN_HTML;
        }

        response.setCookie(LOGIN_SUCCESS_COOKIE);

        return INDEX_HTML;
    }

    private String create(Map<String, String> parameters) {
        if (!userService.create(parameters)) {
            return CREATE_HTML;
        }

        return INDEX_HTML;
    }
}

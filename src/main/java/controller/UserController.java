package controller;

import java.util.Map;

import http.Method;
import http.Request;
import http.Response;
import service.UserService;

public class UserController implements Controller {
    private static final String CREATE_USER_URI = "/users/create(.*)";
    private static final String LOGIN_USER_URI = "/users/login(.*)";
    private static final String USER_LIST_URI = "/users/list(.*)";

    private static final String INDEX_HTML = "/index.html";
    private static final String LOGIN_HTML = "/user/login.html";
    private static final String CREATE_HTML = "/user/form.html";
    private static final String LIST_HTML = "/user/list.html";

    private static final String LOGIN_QUERY = "logined";
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    private final UserService userService;

    public UserController() {
        userService = new UserService();
    }

    public String run(Request request, Response response) {
        if (request.getMethod().equals(Method.POST.toString())) {
            return postHandler(request, response);
        }

        if (request.getMethod().equals(Method.GET.toString())) {
            return getHandler(request, response);
        }

        return INDEX_HTML;
    }

    private String postHandler(Request request, Response response) {
        String url = request.getUrl();
        Map<String, String> parameters = request.getParameters();
        if (url.matches(CREATE_USER_URI)) {
            return create(parameters);
        }

        if (url.matches(LOGIN_USER_URI)) {
            return login(parameters, response);
        }

        return INDEX_HTML;
    }

    private String getHandler(Request request, Response response) {
        String url = request.getUrl();

        if (url.matches(USER_LIST_URI)) {
            return listUser(request, response);
        }

        return INDEX_HTML;
    }

    private String login(Map<String, String> parameters, Response response) {
        if (!userService.login(parameters)) {
            response.setCookie(LOGIN_QUERY, FALSE);
            return LOGIN_HTML;
        }

        response.setCookie(LOGIN_QUERY, TRUE);
        response.setRedirect();

        return INDEX_HTML;
    }

    private String create(Map<String, String> parameters) {
        if (!userService.create(parameters)) {
            return CREATE_HTML;
        }

        return INDEX_HTML;
    }

    private String listUser(Request request, Response response) {
        if (!request.getCookie().get(LOGIN_QUERY).equals(TRUE)) {
            return LOGIN_HTML;
        }

        response.addAttribute("Users", userService.listAll());
        return LIST_HTML;
    }
}

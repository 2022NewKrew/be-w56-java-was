package controller;

import controller.exception.ControllerMismatchException;
import controller.util.UserHtmlUtils;
import domain.user.dto.UserCreate;
import domain.user.dto.UserInfo;
import domain.user.service.UserService;
import http.HttpHeader;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import util.HttpRequestUtils;

public class UserController implements Controller {

    private static final String USER_LIST_PATH = "/user/list";
    private static final String USER_CREATE_PATH = "/user/create";

    private static final String USER_LOGIN_URL = "/user/login.html";
    private static final String USER_CREATE_REDIRECT_URL = "/index.html";

    private final UserService userService;

    public static UserController create() {
        return new UserController(UserService.create());
    }

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public HttpResponse doGet(HttpRequest request) {
        String path = request.getPath();
        if (!path.equals(USER_LIST_PATH)) {
            throw new ControllerMismatchException("Request Path :: " + request.getPath());
        }

        Map<String, String> cookies = HttpRequestUtils.parseCookies(request.getHeader("Cookie"));
        String authentication = cookies.getOrDefault("logined", "false");

        if (!Boolean.parseBoolean(authentication)) {
            HttpHeader httpHeader = HttpHeader.of(Map.of("Location", USER_LOGIN_URL));

            return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .header(httpHeader)
                .build();
        }

        List<UserInfo> userInfos = userService.readAll();

        String body = UserHtmlUtils.generateUsersHtml(userInfos);

        return HttpResponse.builder()
            .status(HttpStatus.OK)
            .responseBody(body.getBytes(StandardCharsets.UTF_8))
            .build();
    }

    @Override
    public HttpResponse doPost(HttpRequest request) {
        String path = request.getPath();
        if (!path.equals(USER_CREATE_PATH)) {
            return badRequest();
        }

        userService.create(UserCreate.builder()
            .userId(request.getBodyParameter("userId"))
            .password(request.getBodyParameter("password"))
            .name(request.getBodyParameter("name"))
            .email(request.getBodyParameter("email"))
            .build());

        HttpHeader httpHeader = HttpHeader.of(Map.of("Location", USER_CREATE_REDIRECT_URL));

        return HttpResponse.builder()
            .status(HttpStatus.FOUND)
            .header(httpHeader)
            .build();
    }
}

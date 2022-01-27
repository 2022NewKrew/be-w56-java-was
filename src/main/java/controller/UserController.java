package controller;

import domain.user.dto.UserCreate;
import domain.user.service.UserService;
import http.HttpHeader;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import java.util.Map;

public class UserController implements Controller {

    private static final String USER_CREATE_PATH = "/user/create";
    private static final String USER_CREATE_REDIRECT_URL = "/index.html";

    private final UserService userService;

    public static UserController create() {
        return new UserController(UserService.create());
    }

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public HttpResponse doPost(HttpRequest request) {
        String path = request.getPath();
        if (!USER_CREATE_PATH.equals(path)) {
            return badRequest();
        }

        userService.create(UserCreate.builder()
            .userId(request.getRequestBody("userId"))
            .password(request.getRequestBody("password"))
            .name(request.getRequestBody("name"))
            .email(request.getRequestBody("email"))
            .build());

        HttpHeader httpHeader = HttpHeader.of(Map.of("Location", USER_CREATE_REDIRECT_URL));

        return HttpResponse.builder()
            .status(HttpStatus.FOUND)
            .header(httpHeader)
            .build();
    }
}

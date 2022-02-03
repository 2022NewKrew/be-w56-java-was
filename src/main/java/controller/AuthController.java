package controller;

import domain.user.dto.UserLogin;
import domain.user.service.AuthService;
import http.HttpHeader;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import java.util.Map;

public class AuthController implements Controller {

    private static final String USER_LOGIN_PATH = "/user/login";
    private static final String USER_LOGIN_SUCCESS_REDIRECT_URL = "/index.html";
    private static final String USER_LOGIN_FAILED_REDIRECT_URL = "/user/login_failed.html";

    private final AuthService authService;

    public static AuthController create() {
        return new AuthController(AuthService.create());
    }

    private AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public HttpResponse doPost(HttpRequest request) {
        if (!request.getPath().equals(USER_LOGIN_PATH)) {
            return badRequest();
        }

        boolean authentication = authService.login(UserLogin.builder()
            .userId(request.getBodyParameter("userId"))
            .password(request.getBodyParameter("password"))
            .build());

        String redirectUrl =
            authentication ? USER_LOGIN_SUCCESS_REDIRECT_URL : USER_LOGIN_FAILED_REDIRECT_URL;

        HttpHeader httpHeader = HttpHeader.of(
            Map.of("Set-Cookie", "logined=" + authentication + "; Path=/",
                "Location", redirectUrl));

        return HttpResponse.builder()
            .status(HttpStatus.FOUND)
            .header(httpHeader)
            .build();
    }
}

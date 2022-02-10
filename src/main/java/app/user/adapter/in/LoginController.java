package app.user.adapter.in;

import app.user.application.port.in.LoginUseCase;
import app.user.application.port.in.LoginUserDto;
import app.user.exception.WrongPasswordException;
import java.util.Map;
import template.Model;
import webserver.WebServerConfig;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseStatus;
import webserver.servlet.HttpControllable;
import webserver.util.HttpRequestUtils;

public class LoginController implements HttpControllable {

    public static final String path = "/user/login";
    private final LoginUseCase loginUseCase;

    public LoginController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Override
    public String call(HttpRequest request, HttpResponse response, Model model) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(request.getBody());
        try {
            LoginUserDto loginUserDto = new LoginUserDto(params.get("userId"),
                params.get("password"));
            loginUseCase.login(loginUserDto);

            response.setCookie(WebServerConfig.LOGIN_SUCCESS_COOKIE + WebServerConfig.COOKIE_PATH);
            response.setStatus(HttpResponseStatus.FOUND);
            return "/index";
        } catch (WrongPasswordException e) {
            response.setCookie(WebServerConfig.LOGIN_FAILED_COOKIE);
            return "/user/login_failed";
        }
    }
}

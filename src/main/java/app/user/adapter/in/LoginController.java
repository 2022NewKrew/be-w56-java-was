package app.user.adapter.in;

import app.user.application.port.in.LoginUseCase;
import app.user.application.port.in.LoginUserDto;
import app.user.domain.UserId;
import app.user.exception.WrongPasswordException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseStatus;
import webserver.servlet.HttpControllable;
import webserver.util.HttpRequestUtils;

public class LoginController implements HttpControllable {

    public static final String path = "/user";
    private final LoginUseCase loginUseCase;


    public LoginController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Override
    public void call(HttpRequest request, HttpResponse response) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(request.getBody());
        try {
            LoginUserDto loginUserDto = new LoginUserDto(new UserId(params.get("userId")),
                params.get("password"));
            loginUseCase.login(loginUserDto);
        } catch (WrongPasswordException e) {
            response.setStatus(HttpResponseStatus.UNAUTHORIZED);
            response.setBody(e.getMessage().getBytes(StandardCharsets.UTF_8));
            response.send();
        }
    }
}

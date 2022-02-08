package app.user.adapter.in;

import app.user.application.port.in.LoginUseCase;
import app.user.application.port.in.LoginUserDto;
import app.user.domain.UserId;
import app.user.exception.WrongPasswordException;
import com.google.common.net.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import webserver.WebServerConfig;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseStatus;
import webserver.http.MimeSubtype;
import webserver.servlet.HttpControllable;
import webserver.util.HttpRequestUtils;

public class LoginController implements HttpControllable {

    public static final String path = "/user/login";
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

            response.setStatus(HttpResponseStatus.FOUND);
            response.headers()
                .set(HttpHeaders.CONTENT_TYPE, MimeSubtype.TEXT_HTML)
                .set(HttpHeaders.LOCATION, WebServerConfig.ENDPOINT + WebServerConfig.ENTRY_FILE);
            response.setCookie(WebServerConfig.LOGIN_SUCCESS_COOKIE);
        } catch (WrongPasswordException e) {
            response.setStatus(HttpResponseStatus.FOUND);
            response.headers()
                .set(HttpHeaders.LOCATION, WebServerConfig.ENDPOINT + "/user/login_failed.html");
            response.setCookie(WebServerConfig.LOGIN_FAILED_COOKIE);
            response.setBody(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }
}

package adaptor.in.web.user;

import adaptor.in.web.exception.FileNotFoundException;
import adaptor.in.web.exception.UriNotFoundException;
import adaptor.in.web.model.RequestPath;
import application.exception.user.AlreadyExistingUserException;
import application.exception.user.NonExistsUserIdException;
import application.in.user.LoginUseCase;
import application.in.user.SignUpUserUseCase;
import domain.user.User;
import infrastructure.config.ServerConfig;
import infrastructure.model.*;
import infrastructure.util.HttpRequestUtils;
import infrastructure.util.HttpResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final SignUpUserUseCase signUpUserUseCase;
    private final LoginUseCase loginUseCase;

    public UserController(SignUpUserUseCase signUpUserUseCase, LoginUseCase loginUseCase) {
        this.signUpUserUseCase = signUpUserUseCase;
        this.loginUseCase = loginUseCase;
    }

    public HttpResponse handle(HttpRequest httpRequest) throws FileNotFoundException, UriNotFoundException {
        Path path = httpRequest.getRequestPath();
        RequestMethod method = httpRequest.getMethod();
        log.debug("User Controller: {}", path);

        try {
            if (RequestPath.SIGN_UP.equalsValue(path) && method.equals(RequestMethod.POST)) {
                return signUp(httpRequest);
            }
            if (RequestPath.LOGIN.equalsValue(path) && method.equals(RequestMethod.POST)) {
                return login(httpRequest);
            }
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        throw new UriNotFoundException();
    }

    private HttpResponse signUp(HttpRequest request) throws IOException {
        Map<String, String> body = HttpRequestUtils.parseBody(((HttpStringBody) request.getRequestBody()).getValue());
        User user = User.builder()
                .userId(body.get("userId"))
                .password(body.get("password"))
                .name(body.get("name"))
                .email(body.get("email"))
                .build();

        try {
            signUpUserUseCase.signUp(user);
        } catch (AlreadyExistingUserException e) {
            log.debug(e.getMessage());

            return HttpResponseUtils.badRequest();
        }

        return HttpResponseUtils.found("/index.html");
    }

    public HttpResponse login(HttpRequest request) throws IOException {
        String userId, password;
        try {
            Map<String, String> body = HttpRequestUtils.parseBody(((HttpStringBody) request.getRequestBody()).getValue());
            userId = body.get("userId");
            password = body.get("password");
        } catch (NullPointerException e) {
            log.debug(e.getMessage());
            return HttpResponseUtils.badRequest();
        }

        try {
            boolean result = loginUseCase.login(userId, password);
            if (result) {
                return HttpResponse.builder()
                        .status(HttpStatus.FOUND)
                        .setHeader("Location", ServerConfig.getAuthority() + "/index.html")
                        .setCookie("logined=true; Path=/")
                        .build();
            }

            return HttpResponse.builder()
                    .status(HttpStatus.FOUND)
                    .setHeader("Location", ServerConfig.getAuthority() + "/user/login_failed.html")
                    .setCookie("logined=false; Path=/")
                    .build();
        } catch (NonExistsUserIdException e) {
            log.debug(e.getMessage());

            return HttpResponse.builder()
                    .status(HttpStatus.FOUND)
                    .setHeader("Location", ServerConfig.getAuthority() + "/user/login_failed.html")
                    .setCookie("logined=false; Path=/")
                    .build();
        }
    }
}

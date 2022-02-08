package adaptor.in.web.user;

import adaptor.in.web.exception.FileNotFoundException;
import adaptor.in.web.exception.UriNotFoundException;
import adaptor.in.web.model.RequestPath;
import application.exception.user.AlreadyExistingUserException;
import application.exception.user.NonExistsUserIdException;
import application.in.session.SetSessionUseCase;
import application.in.user.FindUserUseCase;
import application.in.user.LoginUseCase;
import application.in.user.SignUpUserUseCase;
import domain.user.User;
import infrastructure.config.ServerConfig;
import infrastructure.model.*;
import infrastructure.util.HtmlTemplateUtils;
import infrastructure.util.HttpRequestUtils;
import infrastructure.util.HttpResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final SetSessionUseCase setSessionUseCase;
    private final SignUpUserUseCase signUpUserUseCase;
    private final LoginUseCase loginUseCase;
    private final FindUserUseCase findUserUseCase;

    public UserController(SetSessionUseCase setSessionUseCase, SignUpUserUseCase signUpUserUseCase, LoginUseCase loginUseCase, FindUserUseCase findUserUseCase) {
        this.setSessionUseCase = setSessionUseCase;
        this.signUpUserUseCase = signUpUserUseCase;
        this.loginUseCase = loginUseCase;
        this.findUserUseCase = findUserUseCase;
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
            if (RequestPath.USER_LIST.equalsValue(path) && method.equals(RequestMethod.GET)) {
                return list(httpRequest);
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
                Long sessionId = setSessionUseCase.setSession("loginId", userId);

                return HttpResponse.builder()
                        .status(HttpStatus.FOUND)
                        .setHeader("Location", ServerConfig.getAuthority() + "/index.html")
                        .setCookie("SESSION_ID=" + sessionId + "; Path=/")
                        .build();
            }

            return HttpResponse.builder()
                    .status(HttpStatus.FOUND)
                    .setHeader("Location", ServerConfig.getAuthority() + "/user/login_failed.html")
                    .build();
        } catch (NonExistsUserIdException e) {
            log.debug(e.getMessage());

            return HttpResponse.builder()
                    .status(HttpStatus.FOUND)
                    .setHeader("Location", ServerConfig.getAuthority() + "/user/login_failed.html")
                    .build();
        }
    }

    public HttpResponse list(HttpRequest request) throws IOException {
        List<User> users = findUserUseCase.findAll();
        List<Model> models = users.stream().map(e -> Model.builder()
                        .addAttribute("name", e.getName())
                        .addAttribute("userId", e.getUserId())
                        .addAttribute("email", e.getEmail())
                        .build())
                .collect(Collectors.toList());
        byte[] bytes = HtmlTemplateUtils.getView("/user/list.html", "users", models);

        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .body(new HttpByteArrayBody(bytes))
                .build();
    }
}

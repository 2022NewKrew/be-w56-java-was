package controller.user;

import controller.BaseController;
import model.UserLoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.user.LoginService;
import webserver.exception.AuthenticationFailureException;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;
import webserver.model.HttpStatus;

public class LoginController implements BaseController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService = new LoginService();

    @Override
    public HttpResponse process(HttpRequest request) {
        UserLoginRequest loginRequest = request.getRequestParams().mapModelObject(UserLoginRequest.class);
        log.info("Login Request : {}", loginRequest);

        HttpResponse response;
        try {
            loginService.login(loginRequest);
            response = new HttpResponse("redirect:/index.html");
            response.addCookie("logined", "true");
        } catch (AuthenticationFailureException e) {
            e.printStackTrace();
            response = new HttpResponse("/user/login_failed.html", HttpStatus.UNAUTHORIZED);
            response.addCookie("logined", "false");
        }
        return response;
    }
}

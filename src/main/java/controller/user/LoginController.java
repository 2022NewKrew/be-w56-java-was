package controller.user;

import controller.BaseController;
import model.UserLoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.user.LoginService;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;

public class LoginController implements BaseController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService = new LoginService();

    @Override
    public HttpResponse process(HttpRequest request) {
        UserLoginRequest loginRequest = request.getRequestParams().mapModelObject(UserLoginRequest.class);
        log.info("Login Request : {}", loginRequest);

        loginService.login(loginRequest);
        HttpResponse response = new HttpResponse("redirect:/index.html");
        response.addCookie("logined", "true");
        return response;
    }
}

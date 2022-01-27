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
import webserver.model.ModelAndView;

public class LoginController implements BaseController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService = new LoginService();

    @Override
    public ModelAndView process(HttpRequest request, HttpResponse response) {
        UserLoginRequest loginRequest = request.getRequestParams().mapModelObject(UserLoginRequest.class);
        log.info("Login Request : {}", loginRequest);

        ModelAndView modelAndView;
        try {
            loginService.login(loginRequest);
            modelAndView = new ModelAndView("/index.html", HttpStatus.FOUND);
            response.addCookie("logined", "true");
        } catch (AuthenticationFailureException e) {
            e.printStackTrace();
            modelAndView = new ModelAndView("/user/login_failed.html", HttpStatus.UNAUTHORIZED);
            response.addCookie("logined", "false");
        }
        return modelAndView;
    }
}

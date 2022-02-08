package controller.auth;

import controller.BaseController;
import exception.AuthenticationFailureException;
import lombok.extern.slf4j.Slf4j;
import model.auth.LoginRequest;
import service.auth.LoginService;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.model.ModelAndView;

@Slf4j
public class LoginController implements BaseController {

    private final LoginService loginService = new LoginService();

    @Override
    public ModelAndView process(HttpRequest request, HttpResponse response) {
        LoginRequest loginRequest = request.getRequestParams().mapModelObject(LoginRequest.class);
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

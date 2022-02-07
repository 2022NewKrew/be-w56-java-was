package controller.auth;

import controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import model.auth.SignupRequest;
import service.auth.SignupService;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.model.ModelAndView;

@Slf4j
public class SignupController implements BaseController {

    private final SignupService signupService = new SignupService();

    @Override
    public ModelAndView process(HttpRequest request, HttpResponse response) {
        SignupRequest signupRequest = request.getRequestParams().mapModelObject(SignupRequest.class);
        log.info("Signup Request : {}", signupRequest);

        signupService.signup(signupRequest);
        log.info("Signup Success!");
        return new ModelAndView("/index.html", HttpStatus.FOUND);
    }
}

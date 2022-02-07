package controller.user;

import controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import model.UserSignupRequest;
import service.user.SignupService;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;
import webserver.model.HttpStatus;
import webserver.model.ModelAndView;

@Slf4j
public class SignupController implements BaseController {

    private final SignupService signupService = new SignupService();

    @Override
    public ModelAndView process(HttpRequest request, HttpResponse response) {
        UserSignupRequest signupRequest = request.getRequestParams().mapModelObject(UserSignupRequest.class);
        log.info("Signup Request : {}", signupRequest);

        signupService.signup(signupRequest);
        log.info("Signup Success!");
        return new ModelAndView("/index.html", HttpStatus.FOUND);
    }
}

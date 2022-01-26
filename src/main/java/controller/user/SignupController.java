package controller.user;

import controller.BaseController;
import model.UserSignupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.user.SignupService;
import webserver.model.HttpRequest;

public class SignupController implements BaseController {

    private static final Logger log = LoggerFactory.getLogger(SignupController.class);

    private final SignupService signupService = new SignupService();

    @Override
    public String process(HttpRequest request) {
        UserSignupRequest signupRequest = request.getRequestParams().mapModelObject(UserSignupRequest.class);
        log.info("Signup Request : {}", signupRequest);

        signupService.signup(signupRequest);
        log.info("Signup Success!");
        return "redirect:/index.html";
    }
}

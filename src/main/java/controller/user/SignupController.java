package controller.user;

import controller.BaseController;
import model.UserSignupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.user.SignupService;
import util.ObjectMappingUtils;
import webserver.model.HttpRequest;

public class SignupController implements BaseController {

    private static final Logger log = LoggerFactory.getLogger(SignupController.class);

    private final SignupService signupService = new SignupService();

    @Override
    public String process(HttpRequest request) {
        UserSignupRequest signupRequest = ObjectMappingUtils.mapObject(request.getQueryStrings(), UserSignupRequest.class);
        log.info("Signup Request : {}", signupRequest);

        signupService.signup(signupRequest);

        return "redirect:/index.html";
    }
}

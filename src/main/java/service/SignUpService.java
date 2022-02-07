package service;

import db.DataBase;
import model.SignUpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUpService {

    private static final Logger log = LoggerFactory.getLogger(SignUpService.class);

    public static void signUp(SignUpRequest signUpRequest) {
        User user = signUpRequest.toUser();

        DataBase.addUser(user);

        log.debug("User {} {} {} signed up", user.getUserId(), user.getName(), user.getEmail());
    }
}

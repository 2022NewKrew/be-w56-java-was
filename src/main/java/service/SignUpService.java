package service;

import db.UserStorage;
import java.sql.SQLException;
import model.SignUpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUpService {

    private static final Logger log = LoggerFactory.getLogger(SignUpService.class);

    public static void signUp(SignUpRequest signUpRequest) throws SQLException, ClassNotFoundException {
        User user = signUpRequest.toUser();

        UserStorage.addUser(user);

        log.debug("User {} {} {} signed up", user.getUserId(), user.getName(), user.getEmail());
    }
}

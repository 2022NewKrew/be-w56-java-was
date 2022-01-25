package service.user;

import db.DataBase;
import model.UserSignupRequest;

public class SignupService {

    public void signup(UserSignupRequest request) {
        request.validate();
        DataBase.addUser(request.toUser());
    }

}

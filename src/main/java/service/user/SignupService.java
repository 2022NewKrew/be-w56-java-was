package service.user;

import db.DataBase;
import model.UserSignupRequest;
import org.apache.commons.lang3.ObjectUtils;
import webserver.exception.InvalidInputException;

public class SignupService {

    public void signup(UserSignupRequest request) {
        request.validate();
        if (!ObjectUtils.isEmpty(DataBase.findUserById(request.getUserId()))) {
            throw new InvalidInputException("아이디가 이미 존재합니다.");
        }
        DataBase.addUser(request.toUser());
    }

}

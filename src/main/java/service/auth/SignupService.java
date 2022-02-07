package service.auth;

import db.DataBase;
import exception.InvalidInputException;
import model.auth.SignupRequest;
import org.apache.commons.lang3.ObjectUtils;

public class SignupService {

    public void signup(SignupRequest request) {
        request.validate();
        if (!ObjectUtils.isEmpty(DataBase.findUserById(request.getUserId()))) {
            throw new InvalidInputException("아이디가 이미 존재합니다.");
        }
        DataBase.addUser(request.toUser());
    }

}

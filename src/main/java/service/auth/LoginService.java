package service.auth;

import db.DataBase;
import domain.user.User;
import exception.AuthenticationFailureException;
import model.auth.LoginRequest;
import org.apache.commons.lang3.ObjectUtils;

public class LoginService {

    public void login(LoginRequest request) {
        request.validate();
        login(request.getUserId(), request.getPassword());
    }

    private void login(String userId, String password) {
        User user = DataBase.findUserById(userId);
        if (ObjectUtils.isEmpty(user)) {
            throw new AuthenticationFailureException("아이디가 존재하지 않습니다.");
        }
        if (!password.equals(user.getPassword())) {
            throw new AuthenticationFailureException("비밀번호가 일치하지 않습니다.");
        }
    }
}

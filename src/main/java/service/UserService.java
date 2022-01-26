package service;

import controller.Controller;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserService {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private final DataBase dataBase;

    public UserService(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public void signUp(Map<String, String> parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("유저 가입 정보 부족");
        }

        User user = new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email"));
        log.debug(user.toString() + "회원 가입 완료");
    }
}

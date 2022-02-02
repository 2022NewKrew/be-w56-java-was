package application.service;

import static application.service.UserServiceConstants.*;

import org.h2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.repository.DataBase;
import application.model.User;
import http.request.HttpRequest;
import infrastructure.dto.AppResponse;
import http.common.Status;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public AppResponse getCreate(HttpRequest httpRequest) {
        String userId = httpRequest.getQueryStringParams(ATTRIBUTE_USER_ID);
        String password = httpRequest.getQueryStringParams(ATTRIBUTE_PASSWORD);
        String name = httpRequest.getQueryStringParams(ATTRIBUTE_NAME);
        String email = httpRequest.getQueryStringParams(ATTRIBUTE_EMAIL);

        return commonCreate(userId, password, name, email);
    }


    private AppResponse commonCreate(String userId, String password, String name, String email) {
        if (StringUtils.isNullOrEmpty(userId) || StringUtils.isNullOrEmpty(password)
                || StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(email)) {
            return AppResponse.of(SIGNUP_FAIL_FILE, Status.OK);
        }

        DataBase db = DataBase.getInstance();
        db.addUser(new User(userId, password, name, email));
        log.info("현재까지 가입된 회원 명단 : ");
        for (User user : db.findAll()) {
            log.info("User : " + user.getUserId() + " " + user.getName());
        }

        return AppResponse.of(REDIRECT_PATH, Status.FOUND);
    }

    public AppResponse postCreate(HttpRequest httpRequest) {
        String userId = httpRequest.getBodyParams(ATTRIBUTE_USER_ID);
        String password = httpRequest.getBodyParams(ATTRIBUTE_PASSWORD);
        String name = httpRequest.getBodyParams(ATTRIBUTE_NAME);
        String email = httpRequest.getBodyParams(ATTRIBUTE_EMAIL);

        return commonCreate(userId, password, name, email);
    }

    public AppResponse postLogin(HttpRequest httpRequest) {
        String userId = httpRequest.getBodyParams(ATTRIBUTE_USER_ID);
        String password = httpRequest.getBodyParams(ATTRIBUTE_PASSWORD);

        DataBase db = DataBase.getInstance();
        User findUser = db.findUserById(userId);

        if (findUser != null && findUser.getPassword().equals(password)) {
            AppResponse httpResponse = AppResponse.of(REDIRECT_PATH, Status.FOUND);
            httpResponse.addHeaderAttribute("Set-Cookie", COOKIE_LOGIN + "=true; Path=/");
            return httpResponse;
        }

        AppResponse httpResponse = AppResponse.of(LOGIN_FAIL_FILE, Status.FOUND);
        httpResponse.addHeaderAttribute("Set-Cookie", COOKIE_LOGIN + "=false; Path=/");
        return httpResponse;
    }
}

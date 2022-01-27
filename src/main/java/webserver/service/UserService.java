package webserver.service;

import org.h2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import webserver.common.FileLocation;
import webserver.controller.request.HttpRequest;
import webserver.controller.response.HttpResponse;
import webserver.common.Status;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public HttpResponse getCreate(HttpRequest httpRequest) {
        String userId = httpRequest.getQueryStringParams(UserServiceConst.ATTRIBUTE_USER_ID.key);
        String password = httpRequest.getQueryStringParams(UserServiceConst.ATTRIBUTE_PASSWORD.key);
        String name = httpRequest.getQueryStringParams(UserServiceConst.ATTRIBUTE_NAME.key);
        String email = httpRequest.getQueryStringParams(UserServiceConst.ATTRIBUTE_EMAIL.key);

        return commonCreate(userId, password, name, email);
    }


    private HttpResponse commonCreate(String userId, String password, String name, String email) {
        if (StringUtils.isNullOrEmpty(userId) || StringUtils.isNullOrEmpty(password)
                || StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(email)) {
            return HttpResponse.of(FileLocation.USER_SINGUP_FAIL.path, Status.OK);
        }

        DataBase db = DataBase.getInstance();
        db.addUser(new User(userId, password, name, email));
        log.info("현재까지 가입된 회원 명단 : ");
        for (User user : db.findAll()) {
            log.info("User : " + user.getUserId() + " " + user.getName());
        }

        return HttpResponse.of(FileLocation.INDEX.path, Status.FOUND);
    }

    public HttpResponse postCreate(HttpRequest httpRequest) {
        String userId = httpRequest.getBodyParams(UserServiceConst.ATTRIBUTE_USER_ID.key);
        String password = httpRequest.getBodyParams(UserServiceConst.ATTRIBUTE_PASSWORD.key);
        String name = httpRequest.getBodyParams(UserServiceConst.ATTRIBUTE_NAME.key);
        String email = httpRequest.getBodyParams(UserServiceConst.ATTRIBUTE_EMAIL.key);

        return commonCreate(userId, password, name, email);
    }

    public HttpResponse postLogin(HttpRequest httpRequest) {
        String userId = httpRequest.getBodyParams(UserServiceConst.ATTRIBUTE_USER_ID.key);
        String password = httpRequest.getBodyParams(UserServiceConst.ATTRIBUTE_PASSWORD.key);

        DataBase db = DataBase.getInstance();
        User findUser = db.findUserById(userId);

        if (findUser != null && findUser.getPassword().equals(password)) {
            log.info("로그인 성공! : {}", userId);
            HttpResponse httpResponse = HttpResponse.of(FileLocation.INDEX.path, Status.FOUND);
            httpResponse.addHeaderAttribute("Set-Cookie", UserServiceConst.COOKIE_LOGIN.key + "=true; Path=/");
            return httpResponse;
        }

        HttpResponse httpResponse = HttpResponse.of(FileLocation.USER_LOGIN_FAIL.path, Status.FOUND);
        httpResponse.addHeaderAttribute("Set-Cookie", UserServiceConst.COOKIE_LOGIN.key + "=false; Path=/");
        return httpResponse;
    }
}

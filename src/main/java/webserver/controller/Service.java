package webserver.controller;

import java.io.File;

import org.h2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import webserver.controller.request.HttpRequest;
import webserver.controller.response.HttpResponse;
import webserver.common.Status;

public class Service {
    private static final Logger log = LoggerFactory.getLogger(Service.class);

    public static HttpResponse getHome(HttpRequest httpRequest) {
        return HttpResponse.of("/index.html", Status.OK);
    }

    public static HttpResponse err405(HttpRequest httpRequest) {
        return HttpResponse.of("/error/405.html", Status.NOT_ALLOWED);
    }

    public static HttpResponse getStatic(HttpRequest httpRequest) {
        // 정적 파일이 없으면 404 반환
        File file = new File("./webapp" + httpRequest.getPath());
        if (!file.isFile()) {
            return HttpResponse.of("/error/404.html", Status.NOT_FOUND);
        }
        // 정적 파일이 있으면 파일 반환
        return HttpResponse.of(httpRequest.getPath(), Status.OK);
    }

    public static HttpResponse getCreate(HttpRequest httpRequest) {
        String userId = httpRequest.getQueryStringParams("userId");
        String password = httpRequest.getQueryStringParams("password");
        String name = httpRequest.getQueryStringParams("name");
        String email = httpRequest.getQueryStringParams("email");

        return commonCreate(userId, password, name, email);
    }


    public static HttpResponse postCreate(HttpRequest httpRequest) {
        String userId = httpRequest.getBodyParams("userId");
        String password = httpRequest.getBodyParams("password");
        String name = httpRequest.getBodyParams("name");
        String email = httpRequest.getBodyParams("email");

        return commonCreate(userId, password, name, email);
    }

    public static HttpResponse commonCreate(String userId, String password, String name, String email) {
        if (StringUtils.isNullOrEmpty(userId) || StringUtils.isNullOrEmpty(password)
                || StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(email)) {
            return HttpResponse.of("/user/signup_failed.html", Status.OK);
        }
        DataBase db = DataBase.getInstance();
        db.addUser(new User(userId, password, name, email));
        log.info("현재까지 가입된 회원 명단 : ");
        for (User user : db.findAll()) {
            log.info("User : " + user.getUserId() + " " + user.getName());
        }

        return HttpResponse.of("/index.html", Status.FOUND);
    }

    public static HttpResponse postLogin(HttpRequest httpRequest) {
        String userId = httpRequest.getBodyParams("userId");
        String password = httpRequest.getBodyParams("password");

        DataBase db = DataBase.getInstance();
        User findUser = db.findUserById(userId);

        if (findUser != null && findUser.getPassword().equals(password)) {
            // 쿠키 지정
            log.info("로그인 성공! : {}", userId);
            HttpResponse httpResponse = HttpResponse.of("/index.html", Status.FOUND);
            httpResponse.addHeaderAttribute("Set-Cookie", "logined=true; Path=/");
            return httpResponse;
        }

        HttpResponse httpResponse =HttpResponse.of("/user/login_failed.html", Status.FOUND);
        httpResponse.addHeaderAttribute("Set-Cookie", "logined=false; Path=/");
        return httpResponse;

    }

}

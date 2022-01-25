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
        return HttpResponse.of("/index.html", Status.OK, null);
    }

    public static HttpResponse err405(HttpRequest httpRequest) {
        return HttpResponse.of("/error/405.html", Status.NOT_ALLOWED, null);
    }

    public static HttpResponse getStatic(HttpRequest httpRequest) {
        // 정적 파일이 없으면 404 반환
        File file = new File("./webapp" + httpRequest.getPath());
        if (!file.isFile()) {
            return HttpResponse.of("/error/404.html", Status.NOT_FOUND, null);
        }
        // 정적 파일이 있으면 파일 반환
        return HttpResponse.of(httpRequest.getPath(), Status.OK, null);
    }

    public static HttpResponse getCreate(HttpRequest httpRequest) {
        String userId = httpRequest.getQueryStringParams("userId");
        String password = httpRequest.getQueryStringParams("password");
        String name = httpRequest.getQueryStringParams("name");
        String email = httpRequest.getQueryStringParams("email");

        // 만약 입력 정보가 부족하거나 이미 존재하는 아이디면 -> 에러 페이지
        DataBase db = DataBase.getInstance();
        if (StringUtils.isNullOrEmpty(userId) || StringUtils.isNullOrEmpty(password)
                || StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(email)) {
            return HttpResponse.of("/user/signup_failed.html", Status.OK, null);
        }
        db.addUser(new User(userId, password, name, email));
        log.info("현재까지 가입된 회원 명단 : ");
        for (User user : db.findAll()) {
            log.info("User : " + user.getUserId() + " " + user.getName());
        }

        return HttpResponse.of("/index.html", Status.OK, null);


    }
}

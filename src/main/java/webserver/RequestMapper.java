package webserver;

import controller.Controller;
import controller.UserController;
import network.HttpMethod;
import network.HttpRequest;
import network.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class RequestMapper {

    private static final Logger log = LoggerFactory.getLogger(RequestMapper.class);

    public static Controller requestMapping(HttpRequest httpRequest) {
        HttpMethod httpMethod = httpRequest.getMethod();
        String path = httpRequest.getPath();

        Controller controller = null;
        switch (httpMethod.name()) {
            case "GET" : controller = getMapping(path); break;
            case "POST" : controller = postMapping(path); break;
        }
        return controller == null ? defaultPath : controller;
    }

    private static Controller getMapping(String path) {
        switch (path) {
            case "/" : return UserController.indexView;
            case "/user/signup" : return UserController.signupView;
            case "/user/login" : return UserController.loginView;
            case "/user/login_failed" : return UserController.loginFailedView;
            case "/user/list" : return UserController.userListView;
            default: return null;
        }
    }

    private static Controller postMapping(String path) {
        switch (path) {
            case "/user/create" : return UserController.signup;
            case "/user/login" : return UserController.login;
            default: return null;
        }
    }

    private static final Controller defaultPath = (httpRequest) -> {
        String path = httpRequest.getPath();
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        List<String> headers = HttpResponseUtils.response200(httpRequest, body);
        return new HttpResponse(headers, body);
    };
}

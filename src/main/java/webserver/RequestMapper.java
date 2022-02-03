package webserver;

import controller.UserController;
import network.HttpMethod;
import network.HttpRequest;
import network.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class RequestMapper {

    private static final Logger log = LoggerFactory.getLogger(RequestMapper.class);

    public static HttpResponse requestMapping(HttpRequest httpRequest) throws IOException {
        HttpMethod httpMethod = httpRequest.getMethod();
        String path = httpRequest.getPath();

        if (httpMethod.equals(HttpMethod.GET)) {
            switch (path) {
                case "/" : return UserController.indexView(httpRequest);
                case "/user/signup" : return UserController.signupView(httpRequest);
                case "/user/login" : return UserController.loginView(httpRequest);
                case "/user/login_failed" : return UserController.loginFailedView(httpRequest);
                case "/user/list" : return UserController.userListView(httpRequest);
            }
        }

        if (httpMethod.equals(HttpMethod.POST)) {
            switch (path) {
                case "/user/create" : return UserController.signup(httpRequest);
                case "/user/login" : return UserController.login(httpRequest);
            }
        }

        return defaultPath(httpRequest);
    }

    private static HttpResponse defaultPath(HttpRequest httpRequest) throws IOException {
        String path = httpRequest.getPath();
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        List<String> headers = HttpResponseUtils.response200(httpRequest, body);
        return new HttpResponse(headers, body);
    }
}

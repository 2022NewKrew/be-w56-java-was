package Controller;

import config.config;
import db.DataBase;
import model.LoginInfo;
import model.MyHttpRequest;
import model.MyHttpResponse;
import model.User;
import webserver.enums.HttpMethod;
import webserver.enums.HttpStatus;
import webserver.enums.MIME;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public enum UserController implements Controller {

    SIGN_UP(HttpMethod.POST, "/user/create") {
        @Override
        public MyHttpResponse run(MyHttpRequest request, DataOutputStream dos) throws IOException {
            Map<String, String> userInfo = request.getParameters();

            User newUser = new User(userInfo.get("userId"),
                    userInfo.get("password"),
                    userInfo.get("name"),
                    userInfo.get("email"));
            DataBase.addUser(newUser);

            return new MyHttpResponse.Builder(dos)
                    .setLocation(config.DEFAULT_URI)
                    .setCookie(request.getHeader("Cookie"))
                    .setContentType(MIME.parse(request.getUri()).getContentType())
                    .setStatus(HttpStatus.REDIRECT)
                    .setBody(Files.readAllBytes(new File(config.MAIN_PAGE).toPath()))
                    .build();
        }
    },
    LOGIN(HttpMethod.POST, "/user/login") {
        @Override
        public MyHttpResponse run(MyHttpRequest request, DataOutputStream dos) throws IOException {
            Map<String, String> userInfo = request.getParameters();
            LoginInfo loginInfo = new LoginInfo(userInfo.get("userId"), userInfo.get("password"));
            User user = DataBase.findUserById(loginInfo.getUserId());

            String uri = config.LOGIN_FAIL_URI;
            String cookie = config.LOGIN_FAIL_COOKIE;
            String path = config.LOGIN_FAIL_PAGE;
            if (loginInfo.authorize(user)) {
                uri = config.DEFAULT_URI;
                cookie = config.LOGIN_SUCCESS_COOKIE;
                path = config.MAIN_PAGE;
            }

            return new MyHttpResponse.Builder(dos)
                    .setLocation(uri)
                    .setCookie(cookie)
                    .setContentType(MIME.HTML.getContentType())
                    .setStatus(HttpStatus.REDIRECT)
                    .setBody(Files.readAllBytes(new File(path).toPath()))
                    .build();
        }
    };

    private final HttpMethod method;
    private final String url;

    UserController(HttpMethod method, String url) {
        this.method = method;
        this.url = url;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public String getUrl() {
        return url;
    }
}

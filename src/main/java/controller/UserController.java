package controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ResponseGenerator;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.MIME;
import webserver.http.PathInfo;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public class UserController implements Controller{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public HttpResponse controlRequest(HttpRequest httpRequest) {
        String path = httpRequest.getPath();

        if (path.equals(PathInfo.PATH_USER_CREATE_REQUEST)) {
            Map<String, String> params = httpRequest.getParameters();
            try {
                User user = new User(
                        params.get("userId"),
                        params.get("password"),
                        params.get("name"),
                        URLDecoder.decode(params.get("email"), StandardCharsets.UTF_8));
                log.debug("User: {}", user);
                DataBase.addUser(user);
                return ResponseGenerator.generateResponse302(PathInfo.PATH_USER_LIST_FILE);
            } catch (Exception e) {
                return ResponseGenerator.generateResponse400(e);
            }
        } else if (path.equals(PathInfo.PATH_LOGIN_REQUEST)) {
            Map<String, String> params = httpRequest.getParameters();
            String userId = params.get("userId");
            String password = params.get("password");
            User user = DataBase.findUserById(userId);
            if (user == null) {
                log.debug("Login failed: User not found");
                return ResponseGenerator.generateLoginFailedResponse();
            }
            if (user.getPassword().equals(password)) {
                log.debug("Login success!");
                return ResponseGenerator.generateLoginResponse();
            }
            log.debug("Login failed: Password mismatch");
            return ResponseGenerator.generateLoginFailedResponse();
        } else if(path.equals(PathInfo.PATH_USER_LIST)) {
            log.debug("logined: {}", httpRequest.isLoggedIn());
            if (httpRequest.isLoggedIn()) {
                try {
                    return ResponseGenerator.generateUserListResponse(DataBase.findAll());
                } catch (IOException e) {
                    return ResponseGenerator.generateResponse500();
                }
            }
            return ResponseGenerator.generateResponse302(PathInfo.PATH_LOGIN_PAGE);
        } else if (Arrays.stream(MIME.values()).anyMatch(mime -> mime.isExtensionMatch(path))) {
            return ResponseGenerator.generateStaticResponse(path);
        } else {
            log.debug("Page not found");
            return ResponseGenerator.generateResponse404();
        }
    }
}

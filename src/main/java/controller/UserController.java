package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.ResponseGenerator;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.MIME;
import webserver.http.PathInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class UserController implements Controller{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService = new UserService();

    public HttpResponse controlRequest(HttpRequest httpRequest) {
        String path = httpRequest.getPath();

        if (path.equals(PathInfo.PATH_USER_CREATE_REQUEST)) {
            try {
                userService.store(httpRequest);
                return ResponseGenerator.generateResponse302(PathInfo.PATH_USER_LIST_FILE);
            } catch (Exception e) {
                return ResponseGenerator.generateResponse400(e);
            }
        } else if (path.equals(PathInfo.PATH_LOGIN_REQUEST)) {
            boolean canLogin = userService.canLogIn(httpRequest);
            if (canLogin) {
                log.debug("Login success!");
                return ResponseGenerator.generateLoginResponse();
            }
            log.debug("Login failed");
            return ResponseGenerator.generateLoginFailedResponse();
        } else if(path.equals(PathInfo.PATH_USER_LIST)) {
            log.debug("loggedin: {}", httpRequest.isLoggedIn());
            if (httpRequest.isLoggedIn()) {
                try {
                    return ResponseGenerator.generateUserListResponse(userService.findAll());
                } catch (SQLException e) {
                    log.error("SQL Exception occured");
                    return ResponseGenerator.generateResponse500();
                } catch (IOException e) {
                    log.error("IO Exception occured");
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

package controller;

import db.DataBase;
import model.RequestHeader;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ResponseStatus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static controller.RequestPathMapper.*;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    protected static void userCreatePath(RequestHeader requestHeader, Map<String, String> requestBody, DataOutputStream dos) {
        User user = new User(requestBody.get("userId"), requestBody.get("password"), requestBody.get("name"), requestBody.get("email"));
        DataBase.addUser(user);

        log.info("Added User : {}", DataBase.findUserById(requestBody.get("userId")).toString());
        response302Header(requestHeader.getContentType(), false, dos);
    }

    protected static void userLoginPath(RequestHeader requestHeader, Map<String, String> requestBody, DataOutputStream dos) throws IOException {
        String userId = requestBody.get("userId");
        String password = requestBody.get("password");

        User user = DataBase.findUserById(userId);
        File failedRequest = new File(URL_PREFIX + "/user/login_failed.html");
        if (user == null || !user.getPassword().equals(password)) {
            log.info("Log-in failed : User not Found");
            byte[] body = Files.readAllBytes(failedRequest.toPath());
            responseHeader(ResponseStatus.BAD_REQUEST, requestHeader.getContentType(), dos, body.length);
            responseBody(dos, body);
            return;
        }
        log.info("Log-in Success");
        response302Header(requestHeader.getContentType(), true, dos);
    }
}

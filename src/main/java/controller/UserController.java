package controller;

import db.DataBase;
import model.RequestHeader;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.util.Map;

import static controller.RequestPathMapper.REDIRECT_PATH;
import static controller.RequestPathMapper.response302Header;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    protected static void userCreatePath(RequestHeader requestHeader, Map<String, String> requestBody, DataOutputStream dos) {
        User user = new User(requestBody.get("userId"), requestBody.get("password"), requestBody.get("name"), requestBody.get("email"));
        DataBase.addUser(user);

        log.info("Added User : {}", DataBase.findUserById(requestBody.get("userId")).toString());
        response302Header(requestHeader.getContentType(), REDIRECT_PATH, dos);
    }
}

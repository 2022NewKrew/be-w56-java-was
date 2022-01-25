package controller;

import annotation.RequestMapping;
import db.DataBase;
import http.HttpResponse;
import http.HttpStatus;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // @RequestMapping(value = "/user/create", method = "GET")
    // public HttpResponse createUser(Map<String, String> query) {
    //     User user = new User(
    //             query.get("userId"),
    //             query.get("password"),
    //             query.get("name"),
    //             query.get("email"));
    //     DataBase.addUser(user);
    //     Map<String, String> headers = new HashMap<>();
    //     headers.put("Location", "/index.html");
    //     return new HttpResponse(headers, HttpStatus.Found);
    // }

    @RequestMapping(value = "/user/create", method = "POST")
    public HttpResponse createUser(Map<String, String> query, String body) {
        Map<String, String> userForm = HttpRequestUtils.parseQueryString(body);
        User user = new User(
                userForm.get("userId"),
                userForm.get("password"),
                userForm.get("name"),
                userForm.get("email")
        );
        DataBase.addUser(user);
        log.debug("Create user: {}", user);
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");
        return new HttpResponse(headers, HttpStatus.Found);
    }
}

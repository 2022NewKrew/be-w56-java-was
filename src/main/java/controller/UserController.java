package controller;

import annotation.Controller;
import annotation.CookieValue;
import annotation.RequestBody;
import annotation.RequestMapping;
import db.DataBase;
import http.HttpMethod;
import http.HttpResponse;
import http.HttpStatus;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/users")
    public HttpResponse listUsers(@CookieValue("logined") String logined) {
        System.out.println(logined);
        Collection<User> users = DataBase.findAll();
        return new HttpResponse(users.toString().getBytes(), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/create", method = HttpMethod.POST)
    public HttpResponse createUser(@RequestBody Map<String, String> formData) {
        User user = new User(
                formData.get("userId"),
                formData.get("password"),
                formData.get("name"),
                formData.get("email")
        );
        DataBase.addUser(user);
        log.debug("Create user: {}", user);
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");
        return new HttpResponse(headers, HttpStatus.Found);
    }

    @RequestMapping(value = "/user/login", method = HttpMethod.POST)
    public HttpResponse loginUser(
            @RequestBody Map<String, String> formData
    ) {
        String userId = formData.get("userId");
        String password = formData.get("password");
        Collection<User> users = DataBase.findAll();
        User user = users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .filter(u -> u.getPassword().equals(password))
                .findAny()
                .orElse(null);

        String location;
        boolean authenticated;
        if (user == null) {
            location = "/user/login_failed.html";
            authenticated = false;
        } else {
            location = "/index.html";
            authenticated = true;
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Location", location);
        HttpResponse response = new HttpResponse(headers, HttpStatus.Found);
        response.addCookie("logined", String.valueOf(authenticated));
        return response;
    }
}

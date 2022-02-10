package controller;

import annotation.RequestMapping;
import db.DataBase;
import http.request.HttpRequest;
import http.request.HttpRequestMethod;
import http.response.HttpResponseHeaderKey;
import java.util.Map;
import model.User;
import model.UserLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController extends Controller {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String EMAIL = "email";

    @RequestMapping(value = "/user/signup", method = HttpRequestMethod.GET)
    public String signupForm(HttpRequest httpRequest) {
        return "user/form";
    }

    @RequestMapping(value = "/user/signup", method = HttpRequestMethod.POST)
    public String signup(HttpRequest httpRequest) {
        Map<String, String> data = getData(httpRequest);
        User user = new User(data.get(USER_ID), data.get(PASSWORD), data.get(NAME), data.get(EMAIL));
        DataBase.addUser(user);

        log.info("SignIn Succeeded - {}", user);
        return "redirect:/";
    }

    @RequestMapping(value = "/user/login", method = HttpRequestMethod.GET)
    public String loginForm(HttpRequest httpRequest) {
        return "user/login";
    }

    @RequestMapping(value = "/user/login", method = HttpRequestMethod.POST)
    public String login(HttpRequest httpRequest, Map<HttpResponseHeaderKey, String> headerMap) {
        Map<String, String> data = getData(httpRequest);
        UserLogin user = new UserLogin(data.get(USER_ID), data.get(PASSWORD));
        boolean loginSuccess = DataBase.login(user);

        String cookieValue = loginSuccess ? "logined=true; Path=/" : "logined=false";
        headerMap.put(HttpResponseHeaderKey.SET_COOKIE, cookieValue);

        log.info(loginSuccess ? "Login Failed" : "Login Succeeded - {}", user);
        return "redirect:/";
    }
}

package app.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import app.domain.User;
import app.service.UserService;
import webserver.annotation.Controller;
import webserver.annotation.RequestMapping;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestMethod;
import webserver.http.HttpResponse;
import webserver.util.Constant;
import webserver.util.ModelAndView;

@Controller
public class UserController {
    private static UserController instance = new UserController();

    private final UserService userService;

    private UserController() {
        userService = UserService.getInstance();
    }

    public static UserController getInstance() {
        return instance;
    }

    @RequestMapping(value = "/user/create", method = HttpRequestMethod.POST)
    public ModelAndView userCreate(HttpRequest request, HttpResponse response) {
        Map<String, String> body = request.getBodyParams();
        userService.registerUser(
                User.builder()
                    .userId(body.get("userId"))
                    .password(body.get("password"))
                    .name(body.get("name"))
                    .email(body.get("email"))
                    .build()
        );

        return new ModelAndView("redirect:" + Constant.INDEX_PATH);
    }

    @RequestMapping(value = "/user/login", method = HttpRequestMethod.POST)
    public ModelAndView login(HttpRequest request, HttpResponse response) {
        Map<String, String> body = request.getBodyParams();
        User user = userService.getUser(body.get("userId"));

        if (ObjectUtils.isNotEmpty(user) && StringUtils.equals(user.getPassword(), body.get("password"))) {
            response.setCookie(Constant.LOGIN_CACHE);

            return new ModelAndView("redirect:" + Constant.INDEX_PATH);
        }

        return new ModelAndView("redirect:" + Constant.LOGIN_FAILED_PATH);
    }

    @RequestMapping(value = "/user/logout", method = HttpRequestMethod.GET)
    public ModelAndView logout(HttpRequest request, HttpResponse response) {
        response.setCookie(Constant.LOGOUT_CACHE);

        return new ModelAndView("redirect:" + Constant.INDEX_PATH);
    }

    @RequestMapping(value = "/user/list", method = HttpRequestMethod.GET)
    public ModelAndView userList(HttpRequest request, HttpResponse response) {
        String cookie = request.getCookie();

        if (StringUtils.contains(cookie, Constant.LOGIN_CACHE)) {
            Map<String, String> map = new HashMap<>();
            map.put("\\{\\{userList\\}\\}", userService.getUserListHtml());

            return new ModelAndView(Constant.USER_LIST_PATH, map);
        }

        return new ModelAndView("redirect:" + Constant.LOGIN_PATH);
    }
}

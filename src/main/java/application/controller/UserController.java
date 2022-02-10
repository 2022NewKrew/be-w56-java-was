package application.controller;

import application.domain.UserService;
import framework.util.RequestMapping;
import application.domain.User;
import application.domain.dto.LoginDto;
import framework.modelAndView.ModelAndView;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestUtils;
import webserver.response.HttpResponse;

import java.util.List;
import java.util.Map;

public class UserController implements Controller{

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/user/create", method = "GET")
    public String create(ModelAndView mv, HttpRequest req, HttpResponse res) {
        Map<String, String> parameters = req.getQueryStrings();
        User user = new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email"));
        userService.addUser(user);
        res.setStatusCode(302);
        return "/index.html";
    }

    @RequestMapping(path = "/user/create", method = "POST")
    public String createPOST(ModelAndView mv, HttpRequest req, HttpResponse res) {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(req.getBody());
        User user = new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email"));
        userService.addUser(user);
        res.setStatusCode(302);
        return "/index.html";
    }

    @RequestMapping(path = "/user/login", method = "POST")
    public String login(ModelAndView mv, HttpRequest req, HttpResponse res) {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(req.getBody());
        LoginDto loginDto = new LoginDto(parameters.get("userId"), parameters.get("password"));
        if (!userService.login(loginDto))
            return "/user/login_failed.html";

        res.addHeader("Set-Cookie", "logined=true; Path=/");
        return "redirect:/index.html";
    }

    @RequestMapping(path = "/user/list", method = "GET")
    public String userList(ModelAndView mv, HttpRequest req, HttpResponse res) {
        String cookie = req.getHeaderByKey("Cookie");
        if (!cookie.contains("logined=true"))
            return "/user/login.html";

        List<User> users = userService.getUserList();
        mv.getModel().addAttribute("users", users);
        return "/user/list";
    }
}

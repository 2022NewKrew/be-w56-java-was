package controller;

import dto.UserDto;
import exception.UserException;
import lombok.extern.slf4j.Slf4j;
import service.UserService;
import webserver.annotation.RequestMapping;
import webserver.annotation.RequestMethod;
import webserver.model.Model;
import webserver.model.WebHttpRequest;
import webserver.model.WebHttpResponse;

@Slf4j
public class MainController {
    private static final MainController INSTANCE = new MainController();

    public static MainController getInstance() {
        return INSTANCE;
    }

    private MainController() {
        userService = UserService.getInstance();
    }

    private final UserService userService;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String index() {
        log.info("MainController - index()");
        return "/index.html";
    }

    @RequestMapping(value = {"/index", "/index.html"}, method = RequestMethod.GET)
    public String indexRedirect() {
        return "redirect:/";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(WebHttpResponse httpResponse, UserDto dto) {
        log.info("MainController - login() " + dto);
        try {
            userService.login(dto);
            httpResponse.addCookie("logined", "true");
            return "redirect:/index.html";
        } catch (UserException e) {
            httpResponse.addCookie("logined", "false");
            return "/user/login_failed.html";
        }
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String createUser(WebHttpResponse httpResponse, UserDto dto) {
        log.info("MainController - createUser() " + dto);
        try {
            userService.register(dto);
        } catch (UserException e) {
            return "/user/form.html";
        }
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public String userList(WebHttpRequest httpRequest, WebHttpResponse httpResponse, Model model) {
        log.info("MainController - userList()");
        if (httpRequest.getCookie("logined") == null || !httpRequest.getCookie("logined").equals("true")) {
            return "redirect:/index.html";
        }
        model.addAttribute("users", userService.getAllUser());
        return "/user/list.html";
    }
}

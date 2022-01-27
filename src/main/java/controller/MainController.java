package controller;

import dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import service.UserService;
import webserver.annotation.RequestMapping;
import webserver.annotation.RequestMethod;
import webserver.model.Cookie;
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

    @RequestMapping(value = {"/", "/index", "/index.html"}, method = RequestMethod.GET)
    public String index() {
        log.info("KinaController - index()");
        return "/index.html";
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String createUser(WebHttpResponse httpResponse, UserDto dto) {
        log.info("KinaController - createUser() " + dto);
        userService.register(dto);
        httpResponse.addCookie("logined", "true");
        return "redirect:/index.html";
    }
}

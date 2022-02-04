package mvc.controller;

import framework.params.HttpRequest;
import framework.params.RequestMapping;
import framework.params.Session;
import mvc.dto.UserDto;
import mvc.service.UserService;
import framework.constant.RequestMethod;
import org.slf4j.Logger;

public class UserController implements Controller {

    private final Logger log;
    private final UserService userService;

    public UserController(Logger log, UserService userService) {
        this.log = log;
        this.userService = userService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getDefaultIndexPage(HttpRequest request) {
        return "index";
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public String createWithGetMethod(HttpRequest request) {
        var params = request.getRequestParam();
        UserDto dto = new UserDto(params);
        userService.create(dto);
        return "redirect:/";
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String create(HttpRequest request) {
        var params = request.getRequestBody();
        UserDto dto = new UserDto(params);
        userService.create(dto);
        return "redirect:/";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(HttpRequest request, Session session) {
        var params = request.getRequestBody();
        UserDto dto = new UserDto(params);
        try {
            userService.login(dto);
            session.setAttributes("logined", "true");
            return "redirect:/";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "redirect:/user/login_failed.html";
        }
    }
}

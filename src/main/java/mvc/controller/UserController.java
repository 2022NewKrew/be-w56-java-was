package mvc.controller;

import framework.constant.RequestMethod;
import framework.params.*;
import mvc.dto.UserDto;
import mvc.service.UserService;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

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
        var body = request.getRequestBody();
        log.debug("body ... {}", body);
        UserDto dto = new UserDto(body);
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

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public String getUserList(HttpRequest request, Model model) {
        Cookie cookie = request.getCookie();
        if (!"true".equals(cookie.getAttribute("logined"))) {
            return "redirect:/user/login";
        }
        model.setAttributes("userList", userService.getAllUsers());
        return "user/list";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(HttpRequest request, Model model) {
        UserDto dto = UserDto.builder()
                .userId("asdf")
                .build();
        UserDto dto2 = UserDto.builder()
                .userId("aaa")
                .build();
        UserDto dto3 = UserDto.builder()
                .userId("bbb")
                .build();
        List<UserDto> tmp = Arrays.asList(dto, dto2, dto3);
//        model.setAttributes("user", dto);
        model.setAttributes("user", tmp);
        model.setAttributes("testModelValue", "test!!!");
        return "test";
    }
}

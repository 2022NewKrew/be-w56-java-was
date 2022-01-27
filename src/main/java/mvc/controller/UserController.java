package mvc.controller;

import framework.HttpRequest;
import mvc.dto.UserDto;
import mvc.service.UserService;
import framework.RequestMapping;
import framework.variable.RequestMethod;

public class UserController implements Controller {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getDefaultIndexPage(HttpRequest request) {
        return "index";
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public String createWithGetMethod(HttpRequest request) {
        var params = request.getRequestParam();
        UserDto dto = UserDto.builder()
                .userId(params.get("userId"))
                .password(params.get("password"))
                .name(params.get("name"))
                .email(params.get("email"))
                .build();
        userService.create(dto);
        return "redirect:/";
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String create(HttpRequest request) {
        var params = request.getRequestBody();
        UserDto dto = UserDto.builder()
                .userId(params.get("userId"))
                .password(params.get("password"))
                .name(params.get("name"))
                .email(params.get("email"))
                .build();
        userService.create(dto);
        return "redirect:/";
    }
}

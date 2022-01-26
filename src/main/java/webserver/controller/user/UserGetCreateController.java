package webserver.controller.user;

import dto.UserCreateDto;
import webserver.Request;

public class UserGetCreateController implements UserController {
    @Override
    public String control(Request request) {
        userService.create(UserCreateDto.builder()
                .stringId(request.getParameters().get("stringId"))
                .password(request.getParameters().get("password"))
                .name(request.getParameters().get("name"))
                .email(request.getParameters().get("email"))
                .build());
        return "redirect:/index.html";
    }
}

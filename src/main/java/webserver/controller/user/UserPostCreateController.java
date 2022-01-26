package webserver.controller.user;

import dto.UserCreateDto;
import webserver.Request;

public class UserPostCreateController implements UserController {
    @Override
    public String control(Request request) {
        userService.create(UserCreateDto.builder()
                .stringId(request.getBodyAttributes().get("stringId"))
                .password(request.getBodyAttributes().get("password"))
                .name(request.getBodyAttributes().get("name"))
                .email(request.getBodyAttributes().get("email"))
                .build());
        return "redirect:/index.html";
    }
}

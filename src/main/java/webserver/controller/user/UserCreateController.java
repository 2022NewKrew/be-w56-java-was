package webserver.controller.user;

import dto.UserCreateDto;
import webserver.Request;
import webserver.Response;

public class UserCreateController implements UserController {
    @Override
    public void start(Request request, Response response) {
        userService.create(UserCreateDto.builder()
                .stringId(request.getParameters().get("stringId"))
                .password(request.getParameters().get("password"))
                .name(request.getParameters().get("name"))
                .email(request.getParameters().get("email"))
                .build());
    }
}

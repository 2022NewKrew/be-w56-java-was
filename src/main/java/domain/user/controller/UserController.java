package domain.user.controller;

import domain.user.dto.UserCreateRequest;
import domain.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    public void create(UserCreateRequest userCreateRequest) {
        userService.create(userCreateRequest);
    }
}

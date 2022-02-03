package webserver.controller;

import service.UserService;

public class LoginController {
    private static final LoginController instance = new LoginController();
    private static final UserService userService = UserService.getInstance();

    private LoginController() {}

    public static LoginController getInstance() {
        return instance;
    }
}

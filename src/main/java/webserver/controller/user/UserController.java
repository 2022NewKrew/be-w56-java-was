package webserver.controller.user;

import service.UserService;
import webserver.controller.Controller;

public interface UserController extends Controller {
    UserService userService = new UserService();
}
